package edu.northeastern.groupproject.GameSphere;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.northeastern.groupproject.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTextView, contentTextView, dateTextView, likesTextView;

    public NewsViewHolder(@NonNull View itemView, NewsClickListener newsClickListener) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.newsTitleTextView);
        contentTextView = itemView.findViewById(R.id.newsContentTextView);
        dateTextView = itemView.findViewById(R.id.newsDateTextView);
        likesTextView = itemView.findViewById(R.id.newsLikesTextView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newsClickListener != null && getLayoutPosition() != RecyclerView.NO_POSITION) {
                    newsClickListener.onItemClick(getLayoutPosition());
                }
            }
        });
    }
}
