package edu.northeastern.groupproject.GameSphere;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.northeastern.groupproject.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    public TextView contentTextView, dateTextView, userNameTextView;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        contentTextView = itemView.findViewById(R.id.commentContentTextView);
        dateTextView = itemView.findViewById(R.id.commentDateTextView);
        userNameTextView = itemView.findViewById(R.id.commentUserTextView);
    }
}
