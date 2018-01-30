package com.elainedv.contacts_test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Elaine on 18/1/29.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {


    private ArrayList<Contacts> mList;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener=listener;
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onItemClickListener(view,(int)view.getTag());
        }
    }

    public static interface OnItemClickListener{

        public void onItemClickListener(View v,int position);
    }

    public MyAdapter(Context context, ArrayList<Contacts> list){
        mList=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(mList.get(position).getName()+'\n'+mList.get(position).getNumber());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.tv);
        }
    }

}
