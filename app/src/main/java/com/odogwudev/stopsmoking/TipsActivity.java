package com.odogwudev.stopsmoking;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ryan.rv_gallery.AnimManager;
import com.ryan.rv_gallery.GalleryRecyclerView;
import com.ryan.rv_gallery.util.DLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author RyanLee
 */
public class TipsActivity extends AppCompatActivity implements GalleryRecyclerView.OnItemClickListener, RecyclerAdapter.OnItemPhotoChangedListener {
    public static final String TAG="TipstActicity_TAG";
    private static final String KEY_PRE_DRAW="key_pre_draw";
    private GalleryRecyclerView mRecyclerView;
    private RelativeLayout mContainer;
    private SeekBar mSeekbar;
    private Map<String, Drawable> mTSDraCacheMap=new HashMap<> ();
    /**
     * Get the position of the blurred background
     */
    private int mLastDraPosition=-1;


    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        setContentView (R.layout.activity_tips);

        Toolbar toolbar=findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        setTitle (R.string.tips);

        androidx.appcompat.app.ActionBar actionBar=getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
        }
        //display banner Ad
        AdView adView=findViewById (R.id.adView);
        AdRequest adRequest=new AdRequest.Builder ()
                // TODO: Replace with your own Device ID.
                .addTestDevice ("33BE2250B43518CCDA7DE426D04EE231")
                .build ();
        adView.loadAd (adRequest);
        //end banner Ad

        DLog.setDebug (true);

        DLog.d (TAG, "TipsActivity onCreate()");

        mRecyclerView=findViewById (R.id.rv_list);
        mContainer=findViewById (R.id.rl_container);
        mSeekbar=findViewById (R.id.seekBar);


        final RecyclerAdapter adapter=new RecyclerAdapter (TipsActivity.this, getDatas ());


        mRecyclerView.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter (adapter);
        mRecyclerView
                // Set the sliding speed (pixels / s)
                .initFlingSpeed (9000)
                // Set the margins and the visible width of the left and right pictures, in dp
                .initPageParams (0, 40)
                // Set parameter factors for switching animation
                .setAnimFactor (0.1f)
                // Set switching animation type, currently has AnimManager.ANIM_BOTTOM_TO_TOP and currently has AnimManager.ANIM_TOP_TO_BOTTOM
                .setAnimType (AnimManager.ANIM_BOTTOM_TO_TOP)
                // Set click event
                .setOnItemClickListener (this)
                // Set autoplay
                .autoPlay (false)
                // Set autoplay interval ms
                .intervalTime (2000)
                // Set the location of initialization
                .initPosition (0)
                // After the setup is complete, you must call the setUp () method
                .setUp ();


        //Background Gaussian Blur & Fade
        mRecyclerView.addOnScrollListener (new RecyclerView.OnScrollListener () {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged (recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    setBlurImage (false);

                    mSeekbar.setProgress (mRecyclerView.getScrolledPosition ());
                }
            }
        });
        setBlurImage (false);

        mSeekbar.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener () {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRecyclerView.smoothScrollToPosition (progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//display InterstitialAd
        MainActivity mainActivity=new MainActivity ();
        MainActivity.showInterstitial ();
//End display InterstitialAd

    }


    /**
     * Set the background Gaussian blur
     */
    public void setBlurImage(boolean forceUpdate) {
        RecyclerAdapter adapter=(RecyclerAdapter) mRecyclerView.getAdapter ();
        final int mCurViewPosition=mRecyclerView.getScrolledPosition ();

        boolean isSamePosAndNotUpdate=(mCurViewPosition == mLastDraPosition) && !forceUpdate;

        if (adapter == null || mRecyclerView == null || isSamePosAndNotUpdate) {
            return;
        }
        mRecyclerView.post (new Runnable () {
            @Override
            public void run() {
                //If it is a Fragment, you need to determine whether the Fragment is Attach the current Activity, otherwise getResource will report an error
                /*if (!isAdded()) {
                    // fix fragment not attached to Activity
                    return;
                }*/
                // Get the image resource ID of the current location
                int resourceId=((RecyclerAdapter) Objects.requireNonNull (mRecyclerView.getAdapter ())).getResId (mCurViewPosition);
                // Convert this resource picture to Bitmap
                Bitmap resBmp=BitmapFactory.decodeResource (getResources (), resourceId);
                // Gaussian blur the Bitmap and return to resBlurBmp
                Bitmap resBlurBmp=BlurBitmapUtil.blurBitmap (mRecyclerView.getContext (), resBmp, 15f);
                // Convert resBlurBmp to Drawable
                Drawable resBlurDrawable=new BitmapDrawable (mRecyclerView.getResources (), resBlurBmp);
                // Get the previous page's Drawable
                Drawable preBlurDrawable=mTSDraCacheMap.get (KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get (KEY_PRE_DRAW);

                /* The following is the fade effect */
                Drawable[] drawableArr={preBlurDrawable, resBlurDrawable};
                TransitionDrawable transitionDrawable=new TransitionDrawable (drawableArr);
                mContainer.setBackground (transitionDrawable);
                transitionDrawable.startTransition (500);

                // Stored in cache
                mTSDraCacheMap.put (KEY_PRE_DRAW, resBlurDrawable);
                // Record the location of the last Gaussian blur
                mLastDraPosition=mCurViewPosition;
            }
        });
    }


    /***
     * 测试数据
     * @return List<Integer>
     */
    public List<Integer> getDatas() {
        TypedArray ar=getResources ().obtainTypedArray (R.array.tips_arr);
        final int[] resIds=new int[ar.length ()];
        for (int i=0; i < ar.length (); i++) {
            resIds[i]=ar.getResourceId (i, 0);
        }
        ar.recycle ();
        List<Integer> tDatas=new ArrayList<> ();
        for (int resId : resIds) {
            tDatas.add (resId);
        }
        return tDatas;
    }

    @Override
    protected void onResume() {
        super.onResume ();

        DLog.d (TAG, "TipsActivity onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart ();

        DLog.d (TAG, "TipsActivity onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause ();

        DLog.d (TAG, "TipsActivity onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop ();

        DLog.d (TAG, "TipsActivity onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        DLog.d (TAG, "TipsActivity onDestroy()");

        if (mRecyclerView != null) {
            // Release resources
            mRecyclerView.release ();
        }
    }

    @Override
    public void onItemClick(View view, int position) {


    }

    @Override
    public void onItemPhotoChanged() {
        setBlurImage (true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate (R.menu.menu_main, menu);
        menu.findItem (R.id.action_image).setVisible (false);
        menu.findItem (R.id.action_share).setVisible (false);
        menu.findItem (R.id.action_reset).setVisible (false);
        menu.findItem (R.id.action_info).setVisible (false);
        menu.findItem (R.id.action_filter).setVisible (false);
        menu.findItem (R.id.action_sort).setVisible (false);
        menu.findItem (R.id.action_backup).setVisible (false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                onBackPressed ();
                break;


            case R.id.action_settings:

                Intent intent=new Intent (TipsActivity.this, UserSettingsActivity.class);
                startActivity (intent);
                overridePendingTransition (0, 0);
                break;

        }

        return super.onOptionsItemSelected (item);
    }


}
