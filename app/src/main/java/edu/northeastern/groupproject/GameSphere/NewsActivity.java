package edu.northeastern.groupproject.GameSphere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import edu.northeastern.groupproject.R;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private DatabaseReference dataRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        newsList = new ArrayList<>();
        dataRef = FirebaseDatabase.getInstance().getReference("News");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (newsList.size() == 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Long newsId = snapshot.child("newsId").getValue(Long.class);
                        Long numberOfLikes = snapshot.child("numberOfLikes").child("number").getValue((Long.class));
                        String title = snapshot.child("title").getValue(String.class);
                        String newsDate = snapshot.child("newsDate").getValue(String.class);
                        String content = snapshot.child("content").getValue(String.class);
                        List<Comment> commentList = new ArrayList<>();
                        if (snapshot.hasChild("comments")) {
                            Map<String, Map<String, String>> comments = (Map<String, Map<String, String>>) snapshot.child("comments").getValue();
                            for (String comment : comments.keySet()) {
                                String commentDate = comments.get(comment).get("commentDate");
                                String commentContent = comments.get(comment).get("content");
                                String commentUsername = comments.get(comment).get("username");
                                String location = comments.get(comment).get("location");
                                commentList.add(new Comment(commentUsername, commentContent, commentDate, location));
                            }
                        }
                        News news = new News(newsId, title, content, newsDate, numberOfLikes, commentList);
                        newsList.add(news);
                    }
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        NewsClickListener newsClickListener = new NewsClickListener() {
            @Override
            public void onItemClick(int position) {
                List<Comment> commentList = newsList.get(position).getCommentList();
                    SharedPreferences sharedPreferences = getSharedPreferences("newsInfo", MODE_PRIVATE);
                    sharedPreferences.edit().putString("newsIndex", String.valueOf(position)).apply();
                    sharedPreferences.edit().putString("commentIndex", String.valueOf(commentList.size())).apply();
                    Intent intent = new Intent(NewsActivity.this, CommentActivity.class);
                    intent.putExtra("commentList", (ArrayList) commentList);
                    startActivity(intent);
            }
        };

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(newsList, this);
        newsAdapter.setOnItemClickListener(newsClickListener);
        recyclerView.setAdapter(newsAdapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        Switch locationSwitch = findViewById(R.id.switchLocation);
        Button postButton = findViewById(R.id.btnPost);
        floatingActionButton.setVisibility(View.INVISIBLE);
        locationSwitch.setVisibility(View.INVISIBLE);
        postButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
