package edu.northeastern.groupproject.GameSphere;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import edu.northeastern.groupproject.GameSphere.adapter.RoomAdapter;
import edu.northeastern.groupproject.GameSphere.model.Room;
import edu.northeastern.groupproject.R;

public class RoomActivity extends AppCompatActivity {
    public static String currentUser="";
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView btnAddRoom;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private DatabaseReference dataRef;
    private static final int REQUEST_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        roomList =new ArrayList<>();

        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE);
        currentUser=sp.getString("name","");

        dataRef= FirebaseDatabase.getInstance().getReference("Rooms");
        getFullData();
        RoomClickListener roomClickListener=new RoomClickListener() {
            @Override
            public void onItemClick(int position) {
                // see if user is already in this room
                Intent intent=new Intent(RoomActivity.this, MessageActivity.class);
                intent.putExtra("roomId",roomList.get(position).getRoomId());
                intent.putExtra("members",String.join(",",roomList.get(position).getMembers()));
                intent.putExtra("roomName",roomList.get(position).getRoomName());
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
                if(query.length()<=1){
                    getFullData();
                }else{
                    getQueryData(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query text changes
                return false;
            }
        });

        btnAddRoom=findViewById(R.id.btn_add_room);
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRoom();
            }
        });
    }

    private Room createRoomFromData(DataSnapshot snapshot ){
        String roomId = snapshot.child("roomId").getValue(String.class);
        String image=snapshot.child("image").getValue(String.class);
        String admin=snapshot.child("admin").getValue(String.class);
        String roomDescription=snapshot.child("roomDescription").getValue(String.class);
        String roomName=snapshot.child("roomName").getValue(String.class);
        Long time=snapshot.child("time").getValue(Long.class);

        List<String> members = snapshot.child("members").getValue(new GenericTypeIndicator<List<String>>(){});
        Room room=new Room(roomId,image,roomDescription,roomName,members,time,admin);
        return room;
    }

    private void addNewRoom(){
        Dialog addDialog=new Dialog(RoomActivity.this);
        addDialog.setContentView(R.layout.dialog_add_room);

        EditText addRoomName=addDialog.findViewById(R.id.add_room_name);
        ImageView addRoomImage=addDialog.findViewById(R.id.add_room_image);
        Button btnAddRoomImage=addDialog.findViewById(R.id.btn_add_room_image);
        EditText addRoomDesc=addDialog.findViewById(R.id.add_room_description);

        Button btnAddSave=addDialog.findViewById(R.id.btn_add_save);
        Button btnAddCancel=addDialog.findViewById(R.id.btn_add_cancel);

        btnAddRoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: upload picture
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                photoLauncher.launch(intent);
            }
        });

        btnAddSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("admin",currentUser);
                String image="https://firebasestorage.googleapis.com/v0/b/team5-9cb36.appspot.com/o/RoomImage%2FSuper-Mario-Run-main_tcm25-454905_tcm32-455660.jpg?alt=media&token=b045c1fb-3741-49f9-8968-d397a442bd33"; //TODO: store image into firestore
                hashMap.put("image",image);
                hashMap.put("members",new ArrayList<String>());
                hashMap.put("roomDescription",addRoomDesc.getText().toString());
                hashMap.put("roomName",addRoomName.getText().toString());
                hashMap.put("time",System.currentTimeMillis());
                ArrayList<String> members=new ArrayList<>();
                members.add(currentUser);
                hashMap.put("members",members);
                String roomId=dataRef.push().getKey();
                hashMap.put("roomId",roomId);
                dataRef.child(roomId).setValue(hashMap);
                // TODO: initialize messageRoom
                addDialog.dismiss();
            }
        });
        btnAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.dismiss();
            }
        });

        addDialog.show();

    }
    private void getFullData(){
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Room room=createRoomFromData(snapshot);
//                    Log.v("room",room.toString());
                    roomList.add(room);
//                    Log.v("roomList",roomList.size()+"rooms");
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getQueryData(String keyword) {
        Log.v("get query data","start");
        Query query = dataRef.orderByChild("roomName").equalTo(keyword);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                roomList.clear();
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    Room room=createRoomFromData(dataSnapshot);
                    Log.v("room",room.toString());
                    roomList.add(room);
                }
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    // Create an instance of the ActivityResultLauncher to handle the photo request
    private ActivityResultLauncher<Intent> photoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Get the photo URI from the intent
                    Uri photoUri = result.getData().getData();
                    // Upload the photo to Firestore
                    uploadPhoto(photoUri);
                }
            }
    );

    private void uploadPhoto(Uri photoUri) {
        // Create a reference to the image file in Firestore
        String fileName = UUID.randomUUID().toString();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("RoomImage");

        // Upload the image file to Firestore
        UploadTask uploadTask = storageRef.putFile(photoUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the image file
            Task<Uri> downloadUrlTask = storageRef.getDownloadUrl();
            downloadUrlTask.addOnSuccessListener(downloadUrl -> {
                // TODO: Save the download URL to Firestore or perform other operations with it
//                Log.d(TAG, "Download URL: " + downloadUrl.toString());
            });
        });
    }


}