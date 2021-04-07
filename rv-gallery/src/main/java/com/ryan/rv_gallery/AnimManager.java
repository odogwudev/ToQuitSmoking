package com.ryan.rv_gallery;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 *
 * @author RyanLee
 * @date 2017/12/12
 */

public class AnimManager {

    public static final int ANIM_BOTTOM_TO_TOP = 0;
    private static final int ANIM_TOP_TO_BOTTOM = 1;

    /**
     * Animation type
     */
    private int mAnimType = ANIM_BOTTOM_TO_TOP;
    /**
     * Change factor
     */
    private float mAnimFactor = 0.2f;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    void setAnimation(RecyclerView recyclerView, int position, float percent) {
        if (mAnimType == ANIM_TOP_TO_BOTTOM) {
            setTopToBottomAnim (recyclerView, position, percent);
        } else {
            setBottomToTopAnim (recyclerView, position, percent);
        }
    }


    /**
     * Animation effect from bottom to top
     *
     * @param recyclerView RecyclerView
     * @param position int
     * @param percent float
     */
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    private void setBottomToTopAnim(RecyclerView recyclerView, int position, float percent) {
        // Middle page
        View mCurView = Objects.requireNonNull (recyclerView.getLayoutManager ()).findViewByPosition(position);

        // Left page
        View mLeftView = Objects.requireNonNull (recyclerView.getLayoutManager()).findViewByPosition(position - 1);
        // Right page
        View mRightView = Objects.requireNonNull (recyclerView.getLayoutManager()).findViewByPosition(position + 1);
        // Right Right page
        View mRRView = Objects.requireNonNull (recyclerView.getLayoutManager()).findViewByPosition(position + 2);

        if (mLeftView != null) {
            mLeftView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
            mLeftView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
        }
        if (mCurView != null) {
            mCurView.setScaleX(1 - percent * mAnimFactor);
            mCurView.setScaleY(1 - percent * mAnimFactor);
        }
        if (mRightView != null) {
            mRightView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
            mRightView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
        }
        if (mRRView != null) {
            mRRView.setScaleX(1 - percent * mAnimFactor);
            mRRView.setScaleY(1 - percent * mAnimFactor);
        }
    }


    /***
     * Top-down effect
     * @param recyclerView RecyclerView
     * @param position int
     * @param percent int
     */
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    private void setTopToBottomAnim(RecyclerView recyclerView, int position, float percent) {
        // Middle page
        View mCurView = Objects.requireNonNull (recyclerView.getLayoutManager ()).findViewByPosition(position);
        // Right page
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1);
        // Left page
        View mLeftView = recyclerView.getLayoutManager().findViewByPosition(position - 1);
        // Left left page
        View mLLView = recyclerView.getLayoutManager().findViewByPosition(position - 2);

        if (mLeftView != null) {
            mLeftView.setScaleX(1 - percent * mAnimFactor);
            mLeftView.setScaleY(1 - percent * mAnimFactor);
        }
        if (mCurView != null) {
            mCurView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
            mCurView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
        }
        if (mRightView != null) {
            mRightView.setScaleX(1 - percent * mAnimFactor);
            mRightView.setScaleY(1 - percent * mAnimFactor);
        }
        if (mLLView != null) {
            mLLView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
            mLLView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
        }
    }

    void setAnimFactor(float mAnimFactor) {
        this.mAnimFactor = mAnimFactor;
    }

    void setAnimType(int mAnimType) {
        this.mAnimType = mAnimType;
    }
}
