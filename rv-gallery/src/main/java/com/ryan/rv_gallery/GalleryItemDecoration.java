package com.ryan.rv_gallery;

import androidx.annotation.NonNull;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ryan.rv_gallery.util.DLog;
import com.ryan.rv_gallery.util.OsUtil;

import java.util.Objects;

/**
 * @author RyanLee
 * @date 2017/12/14
 */

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = "TipsActivity_TAG";

    /**
     * Default margins for each page
     */
    int mPageMargin = 0;
    /**
     * The width of the visible part of the left and right sides of the middle page
     */
    int mLeftPageVisibleWidth = 50;

    int mItemConsumeY = 0;
    int mItemConsumeX = 0;

    private GalleryRecyclerView.OnItemClickListener onItemClickListener;

    private OnItemSizeMeasuredListener mOnItemSizeMeasuredListener;

    GalleryItemDecoration() {
    }

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public void getItemOffsets(@NonNull Rect outRect, final @NonNull View view, final @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        DLog.d(TAG, "GalleryItemDecoration getItemOffset() --> position = " + parent.getChildAdapterPosition(view));

        final int position = parent.getChildAdapterPosition(view);
        final int itemCount = Objects.requireNonNull (parent.getAdapter ()).getItemCount();

        parent.post(new Runnable() {
            @Override
            public void run() {
                if (((GalleryRecyclerView) parent).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    onSetHorizontalParams(parent, view, position, itemCount);
                } else {
                    onSetVerticalParams(parent, view, position, itemCount);
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    private void onSetVerticalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        int itemNewWidth = parent.getWidth();
        int itemNewHeight = parent.getHeight() - OsUtil.dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);

        mItemConsumeY = itemNewHeight + OsUtil.dpToPx(2 * mPageMargin);

        if (mOnItemSizeMeasuredListener != null) {
            mOnItemSizeMeasuredListener.onItemSizeMeasured(mItemConsumeY);
        }

        // Fit page 0 and last page without left and right pages, keep them left and right as same as other items
        int topMargin = position == 0 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
        int bottomMargin = position == itemCount - 1 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);

        setLayoutParams(itemView, 0, topMargin, 0, bottomMargin, itemNewWidth, itemNewHeight);
    }

    /**
     * Set parameters for horizontal scrolling
     *
     * @param parent    ViewGroup
     * @param itemView  View
     * @param position  int
     * @param itemCount int
     */
    private void onSetHorizontalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        int itemNewWidth = parent.getWidth() - OsUtil.dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);
        int itemNewHeight = parent.getHeight();

        mItemConsumeX = itemNewWidth + OsUtil.dpToPx(2 * mPageMargin);

        if (mOnItemSizeMeasuredListener != null) {
            mOnItemSizeMeasuredListener.onItemSizeMeasured(mItemConsumeX);
        }

        DLog.d(TAG, "GalleryItemDecoration onSetHorizontalParams -->" + "parent.width=" + parent.getWidth() + ";mPageMargin=" + OsUtil.dpToPx(mPageMargin)
                + ";mLeftVis=" + OsUtil.dpToPx(mLeftPageVisibleWidth) + ";itemNewWidth=" + itemNewWidth);

        // Fit page 0 and last page without left and right pages, keep them left and right as same as other items
        int leftMargin = position == 0 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
        int rightMargin = position == itemCount - 1 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);

        setLayoutParams(itemView, leftMargin, 0, rightMargin, 0, itemNewWidth, itemNewHeight);
    }

    /**
     * Setting parameters
     *
     * @param itemView   View
     * @param left       int
     * @param top        int
     * @param right      int
     * @param bottom     int
     * @param itemWidth  int
     * @param itemHeight int
     */
    private void setLayoutParams(View itemView, int left, int top, int right, int bottom, int itemWidth, int itemHeight) {

        DLog.d(TAG, "GalleryItemDecoration setLayoutParams -->" + "left=" + left + ";top=" + top
                + ";right=" + right + ";bottom=" + bottom + ";itemWidth=" + itemWidth + ";itemHeight=" + itemHeight);

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        boolean mMarginChange = false;
        boolean mWidthChange = false;
        boolean mHeightChange = false;

        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            mMarginChange = true;
        }
        if (lp.width != itemWidth) {
            lp.width = itemWidth;
            mWidthChange = true;
        }
        if (lp.height != itemHeight) {
            lp.height = itemHeight;
            mHeightChange = true;

        }

        // Because the method is called continuously, it is called only after it really changes
        if (mWidthChange || mMarginChange || mHeightChange) {
            itemView.setLayoutParams(lp);
        }
    }

    void setOnItemClickListener(GalleryRecyclerView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    void setOnItemSizeMeasuredListener(OnItemSizeMeasuredListener itemSizeMeasuredListener) {
        this.mOnItemSizeMeasuredListener = itemSizeMeasuredListener;
    }

    interface OnItemSizeMeasuredListener {
        /**
         * Item size measurement completed
         * @param size int
         */
        void onItemSizeMeasured(int size);
    }
}
