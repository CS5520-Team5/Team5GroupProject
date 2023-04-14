package edu.northeastern.groupproject.GameSphere;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.northeastern.groupproject.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private List<News> newsList;
    private Context context;
    private NewsClickListener newsClickListener;
    private DatabaseReference newsDBRef;
    private String userName;

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
        newsDBRef = FirebaseDatabase.getInstance().getReference("News");
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userName = sp.getString("name","");
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
        String newsId = "news" + news.getNewsId();
        holder.titleTextView.setText(String.valueOf(news.getTitle()));
        holder.contentTextView.setText(String.valueOf(news.getContent()));
        holder.dateTextView.setText(String.valueOf(news.getNewsDate()));
        holder.likesTextView.setText(String.valueOf(news.getNumberOfLikes()));

        // 1. get the user list who have clicked the button
        DatabaseReference usersRef =  newsDBRef.child(newsId).child("numberOfLikes").child("from");
        Map<String, String> userMap = new HashMap<>();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    userMap.put(dataSnapshot.getKey(), "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 2. check if the current user is in the list
        DatabaseReference numberRef =  newsDBRef.child(newsId).child("numberOfLikes").child("number");
        holder.btnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userMap.containsKey(userName)) {
                    Toast.makeText(context.getApplicationContext(), "You already liked this news!", Toast.LENGTH_SHORT).show();
                } else {
                    // 1. increase the number by 1
                    int curLikes = Math.toIntExact(news.getNumberOfLikes());
                    int newLikes = curLikes + 1;
                    numberRef.setValue(newLikes);
                    news.setNumberOfLikes((long) newLikes);
                    newsList.set(position, news);

                    // 2. put the user name into the database
                    usersRef.child(userName).setValue("");

                    Toast.makeText(context.getApplicationContext(), "Thanks for supporting this news!", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();

                }
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
