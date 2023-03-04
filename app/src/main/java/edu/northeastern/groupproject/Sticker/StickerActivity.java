package edu.northeastern.groupproject.Sticker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
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

import edu.northeastern.groupproject.R;

public class StickerActivity extends AppCompatActivity implements View.OnClickListener{

    public static String sender ="";
    public static String recipient ="";
    private DatabaseReference chatRef;
    private List<Sticker> list = new ArrayList<>();
    private DatabaseReference databaseReference;
    private StickerAdapter mAdapter;
    private RecyclerView rvChat;
    public int like_cnt,kiss_cnt, think_cnt,happy_cnt,wipe_cnt,star_cnt;
    public TextView like_tv,kiss_tv, think_tv,happy_tv,wipe_tv, star_tv;
    private ImageView ic_like,ic_kiss,ic_think,ic_happy,ic_wipe,ic_star;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);
        // connect to count textviews
        like_tv=findViewById(R.id.like_count);
        kiss_tv=findViewById(R.id.kiss_count);
        think_tv=findViewById(R.id.think_count);
        happy_tv=findViewById(R.id.happy_count);
        wipe_tv=findViewById(R.id.wipe_count);
        star_tv=findViewById(R.id.star_count);
        // set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // set communication stuff
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
        // set stickers
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
    private void highlightSticker(ImageView v){
        v.setBackgroundColor(Color.GRAY);
        v.getLayoutParams().height = 200;
        v.getLayoutParams().width = 200;
        v.requestLayout();
    }
    @Override
    public void onClick(View view) {
        resetStickersSize();
        clearSelect();
        if (view.getId()==R.id.ic_like){
            checkId = "like";
            highlightSticker(ic_like);
        }else if (view.getId()==R.id.ic_kiss){
            checkId = "kiss";
            highlightSticker(ic_kiss);
        }else if (view.getId()==R.id.ic_think){
            checkId = "think";
            highlightSticker(ic_think);
        }else if (view.getId()==R.id.ic_happy){
            checkId = "happy";
            highlightSticker(ic_happy);
        }else if (view.getId()==R.id.ic_wipe){
            checkId = "wipe";
            highlightSticker(ic_wipe);
        }else if (view.getId()==R.id.ic_star){
            checkId = "star";
            highlightSticker(ic_star);
        }
    }

    private void clearSelect() {
        ic_like .setBackgroundColor(getColor(R.color.white));
        ic_kiss .setBackgroundColor(getColor(R.color.white));
        ic_think .setBackgroundColor(getColor(R.color.white));
        ic_happy .setBackgroundColor(getColor(R.color.white));
        ic_wipe .setBackgroundColor(getColor(R.color.white));
        ic_star .setBackgroundColor(getColor(R.color.white));
    }

    private String checkId = "";

    public void updateCount(Sticker s, String sendUser){
        if (s.getSender().equals(sendUser)){
            switch (s.getImageId()){
                case "like":
                    like_cnt ++;
                    break;
                case "think":
                    think_cnt++;
                    break;
                case "kiss":
                    kiss_cnt++;
                    break;
                case "happy":
                    happy_cnt++;
                    break;
                case "wipe":
                    wipe_cnt++;
                    break;
                case "star" :
                    star_cnt++;
                    break;
                default: Log.d(" updateCount Error: ", "No Match stickers.");
            }
        }
    }

    private void getData(final String fromId, final String toId ){

        chatRef = FirebaseDatabase.getInstance().getReference("StickerHistory");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                like_cnt =0;
                kiss_cnt  =0;
                think_cnt =0;
                happy_cnt  =0;
                wipe_cnt=0;
                star_cnt = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Sticker sticker = snapshot.getValue(Sticker.class);
                    if(sticker.getSender().equals(fromId) && sticker.getRecipient().equals(toId) ||
                            sticker.getSender().equals(toId) && sticker.getRecipient().equals(fromId)){
                        list.add(sticker);
                    }
                    updateCount(sticker,sender);
                }
                like_tv.setText("Like: " + like_cnt);
                kiss_tv.setText("Kiss: " + kiss_cnt);
                think_tv.setText("Think: " + think_cnt);
                happy_tv.setText("Happy: " + happy_cnt);
                wipe_tv.setText("Wipe: " + wipe_cnt);
                star_tv.setText("Star: " + star_cnt);
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