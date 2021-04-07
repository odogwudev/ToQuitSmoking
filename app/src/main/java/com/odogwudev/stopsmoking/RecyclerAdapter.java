package com.odogwudev.stopsmoking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryan.rv_gallery.util.DLog;

import java.util.List;

/**
 * @author RyanLee
 * @date 2017/12/7
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {
    private Context mContext;
    private List<Integer> mDatas;


    RecyclerAdapter(Context mContext, List<Integer> mDatas) {
        this.mContext=mContext;
        this.mDatas=mDatas;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        DLog.d (TipsActivity.TAG, "RecyclerAdapter onAttachedToRecyclerView");
        super.onAttachedToRecyclerView (recyclerView);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DLog.d (TipsActivity.TAG, "RecyclerAdapter onCreateViewHolder" + " width = " + parent.getWidth ());
        View itemView=LayoutInflater.from (mContext).inflate (R.layout.item_gallery, parent, false);
        return new MyHolder (itemView);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        DLog.d (TipsActivity.TAG, "RecyclerAdapter onBindViewHolder" + "--> position = " + position);
        holder.mView.setImageResource (mDatas.get (holder.getAdapterPosition ()));

    }

    @Override
    public int getItemCount() {
        return mDatas.size ();
    }

    /**
     * Get resId of position
     *
     * @param position int
     * @return int
     */
    int getResId(int position) {
        return mDatas == null ? 0 : mDatas.get (position);
    }


    public interface OnItemPhotoChangedListener {
        /**
         * Need to replace background image after partial update
         */
        void onItemPhotoChanged();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        final ImageView mView;


        MyHolder(View itemView) {
            super (itemView);
            mView=itemView.findViewById (R.id.iv_photo);

        }
    }
}
