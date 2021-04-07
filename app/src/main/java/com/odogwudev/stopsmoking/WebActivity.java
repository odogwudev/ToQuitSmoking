package com.odogwudev.stopsmoking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WebActivity extends AppCompatActivity {

    private static final String TAG="WebActivity";
    private static ViewPager mPager;
    private static int currentPage=0;
    private static int NUM_PAGES=0;
    WebView webView;
    private ArrayList<Constants> imageSlidingModelArrayList;
    private int[] myImageList=new int[]{
            R.drawable.can_you_quit,
            R.drawable.sagging_arms,
            R.drawable.lips,
            R.drawable.damaged_teeth,
            R.drawable.stained_fingers,
            R.drawable.hair_loss,
            R.drawable.cataracts,
            R.drawable.psoriasis,
            R.drawable.eye_wrinkles,
            R.drawable.brittle_bones,
            R.drawable.heart_disease,
            R.drawable.reproductive_problems,
            R.drawable.early_menopause,
            R.drawable.oral_cancer,
            R.drawable.lung_cancer,

            R.drawable.age_spots,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_web);
        Toolbar toolbar=findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        setTitle (R.string.effects);

        androidx.appcompat.app.ActionBar actionBar=getSupportActionBar ();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled (true);
        }

        imageSlidingModelArrayList=new ArrayList<> ();
        imageSlidingModelArrayList=populateList ();

        init ();
        webView=findViewById (R.id.webView);
        Bundle extras=getIntent ().getExtras ();
        if (extras != null) {

            String data=extras.getString ("titles");

            String url="file:///android_asset/" + data + ".html";
            webView.loadUrl (url);
            WebSettings webSettings=webView.getSettings ();
            webSettings.setBuiltInZoomControls (true);
            webSettings.setDisplayZoomControls (false);
            webSettings.setDomStorageEnabled (true);


        }

        //display banner Ad
        AdRequest adRequest=new AdRequest.Builder ()
                // TODO: Replace with your ownDevice ID.
                .addTestDevice ("33BE2250B43518CCDA7DE426D04EE231")
                .build ();
        AdView adView=findViewById (R.id.adView);
        adView.loadAd (adRequest);
//end banner Ad

        MainActivity mainActivity=new MainActivity ();
        MainActivity.showInterstitial ();
    }

    private void init() {
        mPager=(ViewPager) findViewById (R.id.pager);
        mPager.setAdapter (new SlidingImage_Adapter (WebActivity.this, imageSlidingModelArrayList));

        CirclePageIndicator indicator=(CirclePageIndicator)
                findViewById (R.id.indicator);

        indicator.setViewPager (mPager);

        final float density=getResources ().getDisplayMetrics ().density;

//Set circle indicator radius
        indicator.setRadius (4 * density);

        NUM_PAGES=imageSlidingModelArrayList.size ();

      //Auto start of viewpager (If you want to stop automatic sliding of an image, comment out below code)
        final Handler handler=new Handler ();
        final Runnable Update=new Runnable () {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage=0;
                }
                mPager.setCurrentItem (currentPage++, true);
            }
        };
        Timer swipeTimer=new Timer ();
        swipeTimer.schedule (new TimerTask () {
            @Override
            public void run() {
                handler.post (Update);
            }
        }, 4000, 4000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener (new ViewPager.OnPageChangeListener () {

            @Override
            public void onPageSelected(int position) {
                currentPage=position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private ArrayList<Constants> populateList() {

        ArrayList<Constants> list=new ArrayList<> ();

        for (int i=0; i < 16; i++) {
            Constants imageModel=new Constants ();
            imageModel.setImage_drawable (myImageList[i]);
            list.add (imageModel);
        }

        return list;
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

                Intent intent=new Intent (WebActivity.this, UserSettingsActivity.class);
                startActivity (intent);
                overridePendingTransition (0, 0);
                break;

        }

        return super.onOptionsItemSelected (item);
    }


}
