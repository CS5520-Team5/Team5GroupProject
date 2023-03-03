package edu.northeastern.groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StickerActivity extends AppCompatActivity implements View.OnClickListener{

    public static String sender ="";
    public static String recipient ="";
    private DatabaseReference chatRef;
    private List<Sticker> list = new ArrayList<>();
    private DatabaseReference databaseReference;
    private StickerAdapter mAdapter;
    private RecyclerView rvChat;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        recipient = getIntent().getStringExtra("toId");
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        sender =  sp.getString("name","");
        rvChat =  findViewById(R.id.rv_chat);
        mAdapter = new StickerAdapter(list, this);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        rvChat.setLayoutManager(mLinearLayout);
        rvChat.setAdapter(mAdapter);


        getData(sender, recipient);
        findViewById(R.id.iv_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendText(checkId);
            }
        });
        ic_like = findViewById(R.id.ic_like);
        ic_like .setOnClickListener(this);
        ic_kiss =  findViewById(R.id.ic_kiss);
        ic_kiss .setOnClickListener(this);
        ic_think =findViewById(R.id.ic_think);
        ic_think.setOnClickListener(this);
        ic_happy= findViewById(R.id.ic_happy);
        ic_happy.setOnClickListener(this);
        ic_wipe =findViewById(R.id.ic_wipe);
        ic_wipe.setOnClickListener(this);
        ic_star = findViewById(R.id.ic_star);
        ic_star.setOnClickListener(this);

    }


    public void resetStickersSize(){
        ic_like.getLayoutParams().height = 150;
        ic_like.getLayoutParams().width = 150;
        ic_like.requestLayout();

        ic_kiss.getLayoutParams().height = 150;
        ic_kiss.getLayoutParams().width = 150;
        ic_kiss.requestLayout();

        ic_think.getLayoutParams().height = 150;
        ic_think.getLayoutParams().width = 150;
        ic_think.requestLayout();

        ic_happy.getLayoutParams().height = 150;
        ic_happy.getLayoutParams().width = 150;
        ic_happy.requestLayout();

        ic_wipe.getLayoutParams().height = 150;
        ic_wipe.getLayoutParams().width = 150;
        ic_wipe.requestLayout();

        ic_star.getLayoutParams().height = 150;
        ic_star.getLayoutParams().width = 150;
        ic_star.requestLayout();

    }
    @Override
    public void onClick(View view) {
        resetStickersSize();
        clearSelect();
        if (view.getId()==R.id.ic_like){
            checkId = "smile";
            ic_like.getLayoutParams().height = 200;
            ic_like.getLayoutParams().width = 200;
            ic_like.requestLayout();
        }else if (view.getId()==R.id.ic_kiss){
            checkId = "kiss";
            ic_kiss.getLayoutParams().height = 200;
            ic_kiss.getLayoutParams().width = 200;
            ic_kiss.requestLayout();
        }else if (view.getId()==R.id.ic_think){
            checkId = "think";
            ic_think.getLayoutParams().height = 200;
            ic_think.getLayoutParams().width = 200;
            ic_think.requestLayout();
        }else if (view.getId()==R.id.ic_happy){
            checkId = "wink";
            ic_happy.getLayoutParams().height = 200;
            ic_happy.getLayoutParams().width = 200;
            ic_happy.requestLayout();
        }else if (view.getId()==R.id.ic_wipe){
            checkId = "expressionless";
            ic_wipe.getLayoutParams().height = 200;
            ic_wipe.getLayoutParams().width = 200;
            ic_wipe.requestLayout();
        }else if (view.getId()==R.id.ic_star){
            checkId = "star";
            ic_star.getLayoutParams().height = 200;
            ic_star.getLayoutParams().width = 200;
            ic_star.requestLayout();
        }
    }
    private ImageView ic_like;
    private ImageView ic_kiss;
    private ImageView ic_think;
    private ImageView ic_happy;
    private ImageView ic_wipe;
    private ImageView ic_star;
    private void clearSelect() {
        ic_like .setBackgroundColor(getColor(R.color.white));
        ic_kiss .setBackgroundColor(getColor(R.color.white));
        ic_think .setBackgroundColor(getColor(R.color.white));
        ic_happy .setBackgroundColor(getColor(R.color.white));
        ic_wipe .setBackgroundColor(getColor(R.color.white));
        ic_star .setBackgroundColor(getColor(R.color.white));
    }

    private String checkId = "";



    private void getData(final String fromId, final String toId ){

        chatRef = FirebaseDatabase.getInstance().getReference("StickerHistory");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Sticker chat = snapshot.getValue(Sticker.class);
                    if(chat.getSender().equals(fromId) && chat.getRecipient().equals(toId) ||
                            chat.getSender().equals(toId) && chat.getRecipient().equals(fromId)){
                        list.add(chat);
                    }
                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                return;
            }
        });
    }



    private void sendText(String msg) {
        if (TextUtils.isEmpty(msg)){
            return;
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("imageId", msg);
        hashMap.put("recipient", recipient);
        hashMap.put("time", System.currentTimeMillis());
        databaseReference.child("StickerHistory").push().setValue(hashMap);

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