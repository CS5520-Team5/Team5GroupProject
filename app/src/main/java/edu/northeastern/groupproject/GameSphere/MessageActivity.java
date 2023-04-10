package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.northeastern.groupproject.GameSphere.adapter.MessageAdapter;
import edu.northeastern.groupproject.GameSphere.model.Member;
import edu.northeastern.groupproject.GameSphere.model.Message;
import edu.northeastern.groupproject.R;

public class MessageActivity extends AppCompatActivity {
    private String roomId;
    private static String sender="";
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private List<Member> memberList;
    private DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageList=new ArrayList<>();
        roomId=(String) getIntent().getStringExtra("roomId");
        dataRef = FirebaseDatabase.getInstance().getReference();
        Query query = dataRef.child("MessageHistory").orderByChild("roomId").equalTo(roomId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                for(DataSnapshot snapshot:snapshots.getChildren()){
                    String content=snapshot.child("content").getValue(String.class);
                    String messageId=snapshot.child("messageId").getValue(String.class);
                    String roomId=snapshot.child("roomId").getValue(String.class);
                    String sender=snapshot.child("sender").getValue(String.class);
                    Long time=snapshot.child("time").getValue(Long.class);
                    Message message=new Message(messageId,content,roomId,sender,time);
                    Log.v("message",message.toString());
                    messageList.add(message);
                }
                Log.v("messagelist",messageList.toString());
                messageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Message Recycler View
        messageRecyclerView =findViewById(R.id.message_list);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter=new MessageAdapter(messageList,this);
        messageRecyclerView.setAdapter(messageAdapter);
        // communication
        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE);
        sender=sp.getString("name","");
    }
    private void sendMessage(String msg) {
        if (TextUtils.isEmpty(msg)){
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("content", msg);
        hashMap.put("roomId", roomId);
        hashMap.put("time", System.currentTimeMillis());
        dataRef.child("MessageHistory").push().setValue(hashMap);

    }
}