package edu.northeastern.groupproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public ImageView image_category;
    public TextView text_category;
    public TextView text_setup;
    public TextView text_delivery;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.image_category=itemView.findViewById(R.id.image_category);
        this.text_category=itemView.findViewById(R.id.text_category);
        this.text_setup=itemView.findViewById(R.id.text_setup);
        this.text_delivery=itemView.findViewById(R.id.text_delivery);
    }
}
