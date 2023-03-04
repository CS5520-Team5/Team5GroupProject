package edu.northeastern.groupproject.Sticker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.groupproject.R;

public class ConversationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> list = new ArrayList<>();
    private String userName;
    private DatabaseReference dataRef;
    private NotificationManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ActionBar actionBar = getSupportActionBar(); // calling the go back bar
        actionBar.setDisplayHomeAsUpEnabled(true); // showing the go back bar
        recyclerView = findViewById(R.id.all_users_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(list, this);
        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String toId = list.get(position).getName();
                Intent intent = new Intent(ConversationActivity.this,StickerActivity.class);
                intent.putExtra("toId",toId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userName =  sp.getString("name","");
        Toast.makeText(getApplicationContext(), "Welcome, " + userName +"!", Toast.LENGTH_SHORT).show();
        getUser();
        checkNotice();
    }


    boolean isFirst;
    private void checkNotice() {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("notice","sticker",NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(notificationChannel);
        }
        DatabaseReference stickerRef = FirebaseDatabase.getInstance().getReference("StickerHistory");
        stickerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Sticker sticker = snapshot.getValue(Sticker.class);
                    if(sticker.getRecipient().equals(userName) ){
                        noticeMsg(sticker);
                    }
                }
                isFirst = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private long latTime;
    private void noticeMsg(Sticker sticker) {
        if (latTime>sticker.getTime()){
            return;
        }
        if (isFirst){
            if (latTime<sticker.getTime()){
                latTime = sticker.getTime();
            }
            return;
        }
        int ic = -1;
        if (sticker.getImageId().equals("like")){
            ic = R.drawable.ic_like;
        } else if (sticker.getImageId().equals("kiss")){
            ic = R.drawable.ic_kiss;
        }else if (sticker.getImageId().equals("think")){
            ic = R.drawable.ic_think;
        }else if (sticker.getImageId().equals("happy")){
            ic = R.drawable.ic_happy;
        }else if (sticker.getImageId().equals("wipe")){
            ic = R.drawable.ic_wipe;
        }else if (sticker.getImageId().equals("star")){
            ic = R.drawable.ic_star;
        }
        Intent intent = new Intent(this,ConversationActivity.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        Notification notification = new NotificationCompat.Builder(this,"notice")
                .setContentText("New sticker added to chat!")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(ic)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),ic))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(1,notification);
    }

    private void getUser() {
        dataRef = FirebaseDatabase.getInstance().getReference().child("User");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (!user.getName().equals(userName)){
                        list.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}