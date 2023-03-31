package edu.northeastern.groupproject.GameSphere.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.northeastern.groupproject.GameSphere.model.Post;
import edu.northeastern.groupproject.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> postList;
    private Context context;
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_post_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView post_image;
        private ImageView post_user_avatar;
        private TextView post_username;
        private TextView post_title;
        private TextView post_likes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image=itemView.findViewById(R.id.post_image);
            post_user_avatar=itemView.findViewById(R.id.post_user_avatar);
            post_username=itemView.findViewById(R.id.post_username);
            post_title=itemView.findViewById(R.id.post_title);
            post_likes=itemView.findViewById(R.id.post_likes);
        }
    }
}