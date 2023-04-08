package edu.northeastern.groupproject.GameSphere;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.northeastern.groupproject.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTextView, contentTextView, dateTextView;
    public Button btnLikes;

    public NewsViewHolder(@NonNull View itemView, NewsClickListener newsClickListener) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.newsTitleTextView);
        contentTextView = itemView.findViewById(R.id.newsContentTextView);
        dateTextView = itemView.findViewById(R.id.newsDateTextView);
        btnLikes = itemView.findViewById(R.id.newsLikesButton);

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
