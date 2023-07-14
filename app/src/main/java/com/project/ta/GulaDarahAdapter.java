package com.project.ta;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GulaDarahAdapter extends RecyclerView.Adapter<GulaDarahAdapter.MyHolder> {
    List<GulaDarahModel> listData;
    Context context;

    public GulaDarahAdapter(List<GulaDarahModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_gula, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.data.setText(listData.get(position).getData());
        holder.date.setText(listData.get(position).getDate());
        holder.status.setText(listData.get(position).getStatus());
        if(listData.get(position).getStatus().equalsIgnoreCase("Normal")){
            Glide.with(context).load(getImage("ring1")).into(holder.ring);
            holder.state.setCardBackgroundColor(context.getResources().getColor(R.color.normal));
        }else if(listData.get(position).getStatus().equalsIgnoreCase("Pra-diabetes")){
            Glide.with(context).load(getImage("ring2")).into(holder.ring);
            holder.state.setCardBackgroundColor(context.getResources().getColor(R.color.pra));
        }else if(listData.get(position).getStatus().equalsIgnoreCase("Diabetes")){
            Glide.with(context).load(getImage("ring3")).into(holder.ring);
            holder.state.setCardBackgroundColor(context.getResources().getColor(R.color.diabetes));
        }else{
            Glide.with(context).load(getImage("ring1")).into(holder.ring);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView data, date, status;
        CardView state;
        ImageView ring;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.dataGulaDarah);
            date = itemView.findViewById(R.id.dateGulaDarah);
            status = itemView.findViewById(R.id.status_gula);
            ring = itemView.findViewById(R.id.ring);
            state = itemView.findViewById(R.id.state);

        }
    }
}
