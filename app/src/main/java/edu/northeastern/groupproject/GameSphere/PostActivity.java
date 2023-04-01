package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.groupproject.GameSphere.adapter.PostAdapter;
import edu.northeastern.groupproject.GameSphere.model.Post;
import edu.northeastern.groupproject.R;

public class PostActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference dataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postList=new ArrayList<>();
        dataRef= FirebaseDatabase.getInstance().getReference("Posts");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String postId = snapshot.child("postId").getValue(String.class);
                    int numberOfLikes = snapshot.child("numberOfLikes").getValue((Integer.class));
                    String title = snapshot.child("title").getValue(String.class);
                    Date postDate = snapshot.child("postDate").getValue(String.class);
                    String content = snapshot.child("content").getValue(String.class);

                    List<Post.Comment> commentList = new ArrayList<>();
                    List<Map<String, String>> comments = (List<Map<String, String>>) snapshot.child("comments").getValue();
                    for (Map<String, String> comment : comments) {
                        String commentDate = comment.get("commentDate");
                        String commentContent = comment.get("content");
                        String commentUsername = comment.get("username");
                        commentList.add(new Post.Comment(commentDate, commentContent, commentUsername));
                    }

                    News news = new News(newsId, title, content, newsDate, numberOfLikes, commentList);
                    newsList.add(news);
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })
    }
}