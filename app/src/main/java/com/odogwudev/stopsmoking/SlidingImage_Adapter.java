package com.odogwudev.stopsmoking;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 23/04/2016.
 */
public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<Constants> imageSlidingModelArrayList;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter(Context context, ArrayList<Constants> imageSlidingModelArrayList) {
        this.context=context;
        this.imageSlidingModelArrayList=imageSlidingModelArrayList;
        inflater=LayoutInflater.from (context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView ((View) object);
    }

    @Override
    public int getCount() {
        return imageSlidingModelArrayList.size ();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout=inflater.inflate (R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView=(ImageView) imageLayout
                .findViewById (R.id.image);


        imageView.setImageResource (imageSlidingModelArrayList.get (position).getImage_drawable ());

        view.addView (imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals (object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}


