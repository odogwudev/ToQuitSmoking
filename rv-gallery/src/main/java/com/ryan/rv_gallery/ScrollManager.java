package com.ryan.rv_gallery;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ryan.rv_gallery.util.DLog;
import com.ryan.rv_gallery.util.OsUtil;

/**
 * @author RyanLee
 * @date 2017/12/8
 */

public class ScrollManager {
    private static final String TAG = "TipstActicity_TAG";

    private GalleryRecyclerView mGalleryRecyclerView;

    private int mPosition = 0;

    /**
     * Consumes distance in the x direction, making the offset the left margin + the visible part width of the left item
     */
    private int mConsumeX = 0;
    private int mConsumeY = 0;

    ScrollManager(GalleryRecyclerView mGalleryRecyclerView) {
        this.mGalleryRecyclerView = mGalleryRecyclerView;
    }

    /**
     * 初始化SnapHelper
     *
     * @param helper int
     */
    void initSnapHelper(int helper) {
        switch (helper) {
            case GalleryRecyclerView.LINEAR_SNAP_HELPER:
                LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
                mLinearSnapHelper.attachToRecyclerView(mGalleryRecyclerView);
                break;
            case GalleryRecyclerView.PAGER_SNAP_HELPER:
                PagerSnapHelper mPagerSnapHelper = new PagerSnapHelper();
                mPagerSnapHelper.attachToRecyclerView(mGalleryRecyclerView);
                break;
            default:
                break;
        }
    }

    /**
     * Listening for RecyclerView swipes
     */
    void initScrollListener() {
        GalleryScrollerListener mScrollerListener = new GalleryScrollerListener();
        mGalleryRecyclerView.addOnScrollListener(mScrollerListener);
    }

    void updateConsume() {
        mConsumeX += OsUtil.dpToPx(mGalleryRecyclerView.getDecoration().mLeftPageVisibleWidth + mGalleryRecyclerView.getDecoration().mPageMargin * 2);
        mConsumeY += OsUtil.dpToPx(mGalleryRecyclerView.getDecoration().mLeftPageVisibleWidth + mGalleryRecyclerView.getDecoration().mPageMargin * 2);
        DLog.d(TAG, "ScrollManager updateConsume mConsumeX=" + mConsumeX);
    }

    class GalleryScrollerListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            DLog.d(TAG, "ScrollManager newState=" + newState);
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (mGalleryRecyclerView.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                onHorizontalScroll(recyclerView, dx);
            } else {
                onVerticalScroll(recyclerView, dy);
            }
        }
    }

    /**
     * Slide vertically
     *
     * @param recyclerView RecyclerView
     * @param dy           int
     */
    private void onVerticalScroll(final RecyclerView recyclerView, int dy) {
        mConsumeY += dy;

        // Let RecyclerView be called after mapping is completed, to avoid the value of GalleryAdapterHelper.mItemHeight
        recyclerView.post(new Runnable() {
            @RequiresApi(api=Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                int shouldConsumeY = mGalleryRecyclerView.getDecoration().mItemConsumeY;

                // Position floating point value (i.e. total consumed distance / theoretical consumed distance per page = a floating point position value)
                float offset = (float) mConsumeY / (float) shouldConsumeY;
                // Get percentage of current page movement
                float percent = offset - ((int) offset);

                mPosition = (int) offset;

                DLog.i(TAG, "ScrollManager offset=" + offset + "; mConsumeY=" + mConsumeY + "; shouldConsumeY=" + mPosition);


                // Animate changes
                mGalleryRecyclerView.getAnimManager().setAnimation(recyclerView, mPosition, percent);
            }
        });
    }

    /**
     *Slide horizontally
     *
     * @param recyclerView RecyclerView
     * @param dx           int
     */
    private void onHorizontalScroll(final RecyclerView recyclerView, final int dx) {
        mConsumeX += dx;

        // Let RecyclerView be called after mapping is completed, to avoid the value of GalleryAdapterHelper.mItemWidth
        recyclerView.post(new Runnable() {
            @RequiresApi(api=Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                int shouldConsumeX = mGalleryRecyclerView.getDecoration().mItemConsumeX;

                // Position floating point value (i.e. total consumed distance / theoretical consumed distance per page = a floating point position value)
                float offset = (float) mConsumeX / (float) shouldConsumeX;

                // Get percentage of current page movement
                float percent = offset - ((int) offset);

                mPosition = (int) offset;

                DLog.i(TAG, "ScrollManager offset=" + offset + "; percent=" + percent + "; mConsumeX=" + mConsumeX + "; shouldConsumeX=" + shouldConsumeX + "; position=" + mPosition);

                // Animate changes
                mGalleryRecyclerView.getAnimManager().setAnimation(recyclerView, mPosition, percent);
            }
        });

    }

    public int getPosition() {
        return mPosition;
    }
}
