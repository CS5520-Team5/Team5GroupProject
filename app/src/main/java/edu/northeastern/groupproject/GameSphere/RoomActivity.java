package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.groupproject.GameSphere.adapter.RoomAdapter;
import edu.northeastern.groupproject.GameSphere.model.Room;
import edu.northeastern.groupproject.R;

public class RoomActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView btnAddRoom;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private DatabaseReference dataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        roomList =new ArrayList<>();


        dataRef= FirebaseDatabase.getInstance().getReference("Rooms");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String roomId = snapshot.child("roomId").getValue(String.class);
                    String image=snapshot.child("image").getValue(String.class);
                    String admin=snapshot.child("admin").getValue(String.class);
                    String roomDescription=snapshot.child("roomDescription").getValue(String.class);
                    String roomName=snapshot.child("roomName").getValue(String.class);
                    Long time=snapshot.child("time").getValue(Long.class);

                    List<String> members = snapshot.child("members").getValue(new GenericTypeIndicator<List<String>>(){});
                    Room room=new Room(roomId,image,roomDescription,roomName,members,time,admin);
                    roomList.add(room);
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        RoomClickListener roomClickListener=new RoomClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(RoomActivity.this, MessageActivity.class);
                intent.putExtra("roomId",roomList.get(position).getRoomId());
                startActivity(intent);
            }
        };
        recyclerView=findViewById(R.id.recycler_view_room);
        roomAdapter=new RoomAdapter(roomList,this);
        roomAdapter.setOnItemClickListener(roomClickListener);
        recyclerView.setAdapter(roomAdapter);

        searchView=findViewById(R.id.room_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query text changes
                return false;
            }
        });

        btnAddRoom=findViewById(R.id.btn_add_room);
    }

}