package edu.northeastern.groupproject.GameSphere;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import java.util.List;
import edu.northeastern.groupproject.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{

    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.contentTextView.setText(comment.getContent());
        holder.dateTextView.setText(comment.getCommentDate());
        holder.userNameTextView.setText(comment.getUserName());
        holder.userLocation.setText(comment.getCountry());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
