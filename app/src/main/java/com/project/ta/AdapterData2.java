package com.project.ta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterData2 extends RecyclerView.Adapter<AdapterData2.HolderData> {
    List<DataModel> listData;
    Context context;
    private OnArticleListener mOnArticleListener;
//    LayoutInflater inflater;

    public AdapterData2(Context context, List<DataModel> listData, OnArticleListener onArticleListener) {
//        super(context, R.layout.item_data, listData);
        this.listData = listData;
        this.context = context;
        this.mOnArticleListener = onArticleListener;
//        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new HolderData(view, mOnArticleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.textDataKonten.setText(listData.get(position).getKonten());
        holder.textDataTitle.setText(listData.get(position).getTitle());
        holder.textDataDate.setText(listData.get(position).getDate());
        Glide.with(context).load(listData.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textDataKonten, textDataTitle, textDataDate;
        ImageView imageView;
        OnArticleListener onArticleListener;

        public HolderData(@NonNull View itemView, OnArticleListener onArticleListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_v);
            textDataKonten = itemView.findViewById(R.id.dataTextKonten);
            textDataTitle = itemView.findViewById(R.id.dataTextTitle);
            textDataDate = itemView.findViewById(R.id.dataTextDate);
            this.onArticleListener = onArticleListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onArticleListener.onArticleClick(getAdapterPosition());
        }
    }

    public interface OnArticleListener {
        void onArticleClick(int position);
    }
}
