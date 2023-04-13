package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.northeastern.groupproject.GameSphere.adapter.MemberAdapter;
import edu.northeastern.groupproject.GameSphere.adapter.MessageAdapter;
import edu.northeastern.groupproject.GameSphere.model.Member;
import edu.northeastern.groupproject.GameSphere.model.Message;
import edu.northeastern.groupproject.R;

public class MessageActivity extends AppCompatActivity {
    private String roomId,roomName;
    private static String sender="";
    private RecyclerView messageRecyclerView,memberRecyclerView;
    private TextView roomNameView;
    private MessageAdapter messageAdapter;
    private MemberAdapter memberAdapter;
    private List<Message> messageList;
    private List<Member> memberList;
    private HashSet<String> memberKey;
    private DatabaseReference dataRef;
    private HashMap<String,Member> memberMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init
        setContentView(R.layout.activity_message);
        messageList=new ArrayList<>();
        memberList=new ArrayList<>();
        roomId=(String) getIntent().getStringExtra("roomId");
        roomName=(String) getIntent().getStringExtra("roomName");
        roomNameView=findViewById(R.id.room_name);
        roomNameView.setText(roomName);
        String memberKeyStr=(String) getIntent().getStringExtra("members");
        memberKey=new HashSet<String>(Arrays.asList(memberKeyStr.split(",")));
        Log.v("member key set",memberKey.toString());
        dataRef = FirebaseDatabase.getInstance().getReference();
        memberMap=new HashMap<>();

        // Member Recycler View
        getMemberData();
        memberRecyclerView=findViewById(R.id.user_list);
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter=new MemberAdapter(memberList,this);
        memberRecyclerView.setAdapter(memberAdapter);

        // communication
        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE);
        sender=sp.getString("name","");

        // Message Recycler View
        messageRecyclerView =findViewById(R.id.message_list);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter=new MessageAdapter(messageList,this);
        messageRecyclerView.setAdapter(messageAdapter);
        getMessageData();

    }
    private void getMessageData(){
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

                    if(memberMap.containsKey(sender)){
                        String senderName=memberMap.get(sender).getUsername();
                        String avatar=memberMap.get(sender).getImage();
                        message.setSender(senderName);
                        message.setAvatar(avatar);
                    }
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
    }
    private void getMemberData(){

        dataRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                for(DataSnapshot snapshot:snapshots.getChildren()){
                    String key=snapshot.getKey();
                    if(memberKey.contains(key)){
                        String username=snapshot.child("fullname").getValue(String.class);
                        String avatar=snapshot.child("avatar").getValue(String.class);
                        Member member=new Member(key,username,avatar);
                        Log.v("member",member.toString());
                        memberList.add(member);
                        memberMap.put(key,member);
                    }
                }
                memberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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