package edu.northeastern.groupproject.GameSphere;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.groupproject.R;

public class NewsFragment extends Fragment, AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private List<News> mNewsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreate(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_news_fragment, container, false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("News");

        mRecyclerView = findViewById(R.id.all_users_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsAdapter = new NewsAdapter(getActivity(), mNewsList);
        mRecyclerView.setAdapter(mNewsAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNewsList.clear();
                for (DataSnapshot newsSnapshot : dataSnapshot.getChildren()) {
                    News news = newsSnapshot.getValue(News.class);
                    mNewsList.add(news);
                }
                mNewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load news.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}


