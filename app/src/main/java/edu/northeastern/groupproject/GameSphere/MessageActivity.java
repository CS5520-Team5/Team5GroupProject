package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    private ImageView iv_send,sender_avatar;
    private EditText message_input;
    private MessageAdapter messageAdapter;
    private MemberAdapter memberAdapter;
    private List<Message> messageList;
    private List<Member> memberList;
    private HashSet<String> memberKey;
    private DatabaseReference dataRef;
    private HashMap<String,Member> memberMap;
    private NotificationManager manager;

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
        iv_send=findViewById(R.id.iv_send);
        sender_avatar=findViewById(R.id.sender_avatar);
        message_input=findViewById(R.id.message_input);
        String memberKeyStr=(String) getIntent().getStringExtra("members");
        memberKey=new HashSet<String>(Arrays.asList(memberKeyStr.split(",")));
        dataRef = FirebaseDatabase.getInstance().getReference();
        memberMap=new HashMap<>();

        // communication
        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE);
        sender=sp.getString("userkey","");
        String sendername=sp.getString("name","");
        Toast.makeText(getApplicationContext(), "Welcome to the chat! "+sendername, Toast.LENGTH_SHORT).show();
        // Recycler View
        messageRecyclerView =findViewById(R.id.message_list);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter=new MessageAdapter(messageList,this);
        messageRecyclerView.setAdapter(messageAdapter);

        memberRecyclerView=findViewById(R.id.user_list);
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter=new MemberAdapter(memberList,this);
        memberRecyclerView.setAdapter(memberAdapter);
        // Get Data
        getMemberData();
        initNotification();
        // send
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=message_input.getText().toString();
                sendMessage(msg);
            }
        });

    }
    private void getMessageData(){
        Glide.with(this).load(String.valueOf(memberMap.get(sender).getImage())).into(sender_avatar);
        Query query = dataRef.child("MessageHistory").orderByChild("roomId").equalTo(roomId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                messageList.clear();
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
                        memberMap.get(sender).addCount();
                        noticeMsg(message);
                    }
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
                memberList=new ArrayList<>(memberMap.values());
                Comparator<Member> memberComparator = new Comparator<Member>() {
                    @Override
                    public int compare(Member r1, Member r2) {
                        return r2.getCount()-r1.getCount();
                    }
                };
                Collections.sort(memberList,memberComparator);
                memberAdapter.setMemberList(memberList);
                memberAdapter.notifyDataSetChanged();

                isFirst=false;
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
                        memberMap.put(key,member);
                    }
                }
                getMessageData();
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
        message_input.setText("");
        message_input.clearFocus();
        for(Member m:memberMap.values()){
            m.setCount(0);
        }
    }
    boolean isFirst=true;
    private void initNotification() {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("notice","message",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    private long latTime;
    private void noticeMsg(Message m) {
        if(m.getSender()==memberMap.get(sender).getUsername()){
            return;
        }
        if (latTime>m.getTime()){
            return;
        }
        if (isFirst){
            if (latTime<m.getTime()){
                latTime = m.getTime();
            }
            return;
        }
        Intent intent = new Intent(this, MessageActivity.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        Notification notification = new NotificationCompat.Builder(this,"notice")
                .setContentText("New Message added to chat!")
                .setSmallIcon(R.drawable.notifications)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(1,notification);
    }

}