package com.odogwudev.stopsmoking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> titleList;
    private CustomItemClickListener clickListener;
    private ArrayList<Constants> imageModelArrayList;

    public TitleAdapter(Context mContext, ArrayList<String> titleList, ArrayList<Constants> imageModelArrayList, CustomItemClickListener clickListener) {
        this.mContext=mContext;
        this.titleList=titleList;
        this.clickListener=clickListener;
        this.imageModelArrayList=imageModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view=LayoutInflater.from (mContext).inflate (R.layout.raw_item, parent, false);
        final MyViewHolder viewHolder=new MyViewHolder (view);
        view.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick (view, viewHolder.getAdapterPosition ());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titleText.setText (titleList.get (position).replace ("_", ""));
        holder.img.setImageResource (imageModelArrayList.get (position).getImage_drawable ());


    }

    @Override
    public int getItemCount() {
        return titleList.size () & imageModelArrayList.size ();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        ImageView img;

        MyViewHolder(@NonNull View itemView) {
            super (itemView);

            titleText=itemView.findViewById (R.id.textItem);
            img=(ImageView) itemView.findViewById (R.id.img);
        }
    }
}
