package edu.northeastern.groupproject.GameSphere;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import edu.northeastern.groupproject.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private List<News> newsList;
    private Context context;
    private NewsClickListener newsClickListener;
    private DatabaseReference newsDBRef;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
        newsDBRef = FirebaseDatabase.getInstance().getReference("News");
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
        holder.likesTextView.setText(String.valueOf(news.getNumberOfLikes()));

        holder.btnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curLikes = Math.toIntExact(news.getNumberOfLikes());
                int newLikes = curLikes + 1;
                newsDBRef.child("news" + news.getNewsId()).child("numberOfLikes").child("number").setValue(newLikes);
                news.setNumberOfLikes((long) newLikes);
                newsList.set(position, news);
                notifyDataSetChanged();
            }
        });

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
