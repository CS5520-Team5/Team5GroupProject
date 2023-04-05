package edu.northeastern.groupproject.GameSphere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edu.northeastern.groupproject.R;

public class MessageActivity extends AppCompatActivity {
    private String roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        roomId=(String) getIntent().getStringExtra("roomId");
        Log.v("roomid", roomId);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = databaseRef.child("messages").orderByChild("roomId").equalTo("room1").orderByChild("timestamp");
    }
}