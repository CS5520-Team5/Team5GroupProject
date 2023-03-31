package edu.northeastern.groupproject.GameSphere;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.northeastern.groupproject.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private List<News> newsList;
    private Context context;
    private NewsClickListener newsClickListener;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    public void setOnItemClickListener(NewsClickListener newsClickListener) {
        this.newsClickListener = newsClickListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, null);
        return new NewsViewHolder(view, newsClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        News news = newsList.get(position);
        holder.titleTextView.setText(String.valueOf(news.getTitle()));
        holder.contentTextView.setText(String.valueOf(news.getContent()));
        holder.dateTextView.setText(String.valueOf(news.getNewsDate()));
        holder.likesTextView.setText("Likes: " + String.valueOf(news.getNumberOfLikes()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newsClickListener != null) {
                    newsClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
