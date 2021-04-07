package com.ryan.rv_gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ryan.baselib.util.ThreadUtils;
import com.ryan.rv_gallery.util.DLog;

import java.util.Objects;

/**
 * @author RyanLee
 * @date 2017/12/8
 */

public class GalleryRecyclerView extends RecyclerView implements View.OnTouchListener, GalleryItemDecoration.OnItemSizeMeasuredListener {

    private static final String TAG = "TipsActivity_TAG";

    public static final int LINEAR_SNAP_HELPER = 0;
    public static final int PAGER_SNAP_HELPER = 1;
    /**
     * Sliding speed
     */
    private int mFlingSpeed = 1000;
    /**
     * Whether to play automatically
     */
    private boolean mAutoPlay = false;
    /**
     * Autoplay interval
     */
    private int mInterval = 1000;

    private int mInitPos = -1;


    private AnimManager mAnimManager;
    private ScrollManager mScrollManager;
    private GalleryItemDecoration mDecoration;

    private Runnable mAutoPlayTask = new Runnable() {
        @Override
        public void run() {
            if (getAdapter() == null || getAdapter().getItemCount() <= 0) {
                return;
            }

            int position = getScrolledPosition();
            int itemCount = getAdapter().getItemCount();

            int newPosition = (position + 1) % itemCount;
            smoothScrollToPosition(newPosition);

            ThreadUtils.removeCallbacks(this);
            ThreadUtils.runOnUiThread(this, mInterval);
        }
    };

    public GalleryItemDecoration getDecoration() {
        return mDecoration;
    }

    public AnimManager getAnimManager() {
        return mAnimManager;
    }

    public GalleryRecyclerView(Context context) {
        this(context, null);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GalleryRecyclerView);
        int helper = ta.getInteger(R.styleable.GalleryRecyclerView_helper, LINEAR_SNAP_HELPER);
        ta.recycle();

        DLog.d(TAG, "GalleryRecyclerView constructor");

        mAnimManager = new AnimManager ();
        attachDecoration();
        attachToRecyclerHelper(helper);

        //Set up touch monitoring
        setOnTouchListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }


    private void attachDecoration() {
        DLog.d(TAG, "GalleryRecyclerView attachDecoration");

        mDecoration = new GalleryItemDecoration ();
        mDecoration.setOnItemSizeMeasuredListener(this);
        addItemDecoration(mDecoration);
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = balanceVelocity(velocityX);
        velocityY = balanceVelocity(velocityY);
        return super.fling(velocityX, velocityY);
    }

    /**
     * Returns the sliding speed value
     *
     * @param velocity int
     * @return int
     */
    private int balanceVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, mFlingSpeed);
        } else {
            return Math.max(velocity, -mFlingSpeed);
        }
    }

    /**
     * Connect RecyclerHelper
     *
     * @param helper int
     */
    private void attachToRecyclerHelper(int helper) {
        DLog.d(TAG, "GalleryRecyclerView attachToRecyclerHelper");

        mScrollManager = new ScrollManager (this);
        mScrollManager.initScrollListener();
        mScrollManager.initSnapHelper(helper);
    }

    /**
     * Set page parameters in dp
     *
     * @param pageMargin           Default: 0dp
     * @param leftPageVisibleWidth Default: 50dp
     * @return GalleryRecyclerView
     */
    public GalleryRecyclerView initPageParams(int pageMargin, int leftPageVisibleWidth) {
        mDecoration.mPageMargin = pageMargin;
        mDecoration.mLeftPageVisibleWidth = leftPageVisibleWidth;
        return this;
    }

    /**
     * Set the sliding speed (pixels / s)
     *
     * @param speed int
     * @return GalleryRecyclerView
     */
    public GalleryRecyclerView initFlingSpeed(@IntRange(from = 0) int speed) {
        this.mFlingSpeed = speed;
        return this;
    }

    /**
     * Set Animation Factor
     *
     * @param factor float
     * @return GalleryRecyclerView
     */
    public GalleryRecyclerView setAnimFactor(@FloatRange(from = 0f) float factor) {
        mAnimManager.setAnimFactor(factor);
        return this;
    }

    /**
     * Set animation type
     *
     * @param type int
     * @return GalleryRecyclerView
     */
    public GalleryRecyclerView setAnimType(int type) {
        mAnimManager.setAnimType(type);
        return this;
    }

    /**
     * Set click event
     *
     * @param mListener OnItemClickListener
     */
    public GalleryRecyclerView setOnItemClickListener(OnItemClickListener mListener) {
        if (mDecoration != null) {
            mDecoration.setOnItemClickListener(mListener);
        }
        return this;
    }

    /**
     * Whether to scroll automatically
     *
     * @param auto boolean
     * @return GalleryRecyclerView
     */
    public GalleryRecyclerView autoPlay(boolean auto) {
        this.mAutoPlay = auto;
        return this;
    }

    /**
     * Autoplay
     */
    private void autoPlayGallery() {
        if (mAutoPlay) {
            ThreadUtils.removeCallbacks(mAutoPlayTask);
            ThreadUtils.runOnUiThread(mAutoPlayTask, mInterval);
        }
    }

    /**
     * Remove Autoplay Runnable
     */
    private void removeAutoPlayTask() {
        if (mAutoPlay) {
            ThreadUtils.removeCallbacks(mAutoPlayTask);
        }
    }

    /**
     * load
     *
     * @return GalleryRecyclerView
     */
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    public GalleryRecyclerView setUp() {
        if (Objects.requireNonNull (getAdapter ()).getItemCount() <= 0) {
            return this;
        }

        smoothScrollToPosition(0);
        mScrollManager.updateConsume();

        autoPlayGallery();

        return this;
    }

    /**
     * Release resources
     */
    public void release() {
        removeAutoPlayTask();
    }


    public int getOrientation() {

        if (getLayoutManager() instanceof LinearLayoutManager) {
            if (getLayoutManager() instanceof GridLayoutManager) {
                throw new RuntimeException("Please set LayoutManager to LinearLayoutManager");
            } else {
                return ((LinearLayoutManager) getLayoutManager()).getOrientation();
            }
        } else {
            throw new RuntimeException("Please set LayoutManager to LinearLayoutManager");
        }
    }

    public int getScrolledPosition() {
        if (mScrollManager == null) {
            return 0;
        } else {
            return mScrollManager.getPosition();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        DLog.w(TAG, "GalleryRecyclerView onSaveInstanceState()");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);

        // If it is a horizontal and vertical screen switch (fragment destruction), you should not go smoothScrollToPosition (0), because this method will cause the onHorizontalScroll of the ScrollManager to be continuously executed, and ScrollManager.mConsumeX has been reset, which will cause this value to be disorder
        // And if you take the scrollToPosition (0) method, it will not cause the onHorizontalScroll of the ScrollManager to execute, so the value of ScrollManager.mConsumeX will not be wrong
        scrollToPosition(0);
        // But because the onHorizontalScroll of the ScrollManager is not taken, the switching animation will not be performed, so I call smoothScrollBy (int dx, int dy), let the item slide slightly, and trigger the animation
        smoothScrollBy(10, 0);
        smoothScrollBy(0, 0);

        autoPlayGallery();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                removeAutoPlayTask();
                break;
            case MotionEvent.ACTION_UP:
                autoPlayGallery();
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Play interval time ms
     *
     * @param interval int
     * @return GalleryRecyclerView
     */
    public GalleryRecyclerView intervalTime(@IntRange(from = 10) int interval) {
        this.mInterval = interval;
        return this;
    }

    /**
     * Starting position
     *
     * @param i int
     * @return GalleryRecyclerView
     */
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    public GalleryRecyclerView initPosition(@IntRange(from = 0) int i) {
        if (i >= Objects.requireNonNull (getAdapter ()).getItemCount()) {
            i = getAdapter().getItemCount() - 1;
        } else if (i < 0) {
            i = 0;
        }
        mInitPos = i;
        return this;
    }

    @Override
    public void onItemSizeMeasured(int size) {
        if (mInitPos < 0) {
            return;
        }
        if (mInitPos == 0) {
            scrollToPosition(0);
        } else {
            if (getOrientation() == LinearLayoutManager.HORIZONTAL) {
                smoothScrollBy(mInitPos * size, 0);
            } else {
                smoothScrollBy(0, mInitPos * size);
            }
        }
        mInitPos = -1;
    }

    public interface OnItemClickListener {
        /**
         * Click event
         *
         * @param view     View
         * @param position int
         */
        void onItemClick(View view, int position);
    }
}
