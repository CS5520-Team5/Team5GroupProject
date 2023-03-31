package edu.northeastern.groupproject.GameSphere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.northeastern.groupproject.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;
    private Context context;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, null));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.titleTextView.setText(String.valueOf(news.getTitle()));
        holder.contentTextView.setText(String.valueOf(news.getContent()));
        holder.dateTextView.setText(String.valueOf(news.getNewsDate()));
        holder.likesTextView.setText("Likes: " + String.valueOf(news.getNumberOfLikes()));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView, contentTextView, dateTextView, likesTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.newsTitleTextView);
            contentTextView = itemView.findViewById(R.id.newsContentTextView);
            dateTextView = itemView.findViewById(R.id.newsDateTextView);
            likesTextView = itemView.findViewById(R.id.newsLikesTextView);
        }
    }
}
