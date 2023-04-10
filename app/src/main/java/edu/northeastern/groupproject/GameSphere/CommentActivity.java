package edu.northeastern.groupproject.GameSphere;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.northeastern.groupproject.R;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> commentList;
    FloatingActionButton floatingActionButton;
    TextView commentText;
    Button postButton;
    boolean commentTextVisibility = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private int priority;
    private Double preLatitude;
    private Double preLongitude;
    private boolean isUpdateLocation;
    private boolean switchOn = false;
    private String country = "Unknown";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        commentList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentList = (ArrayList<Comment>) getIntent().getSerializableExtra("commentList");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList, this);
        recyclerView.setAdapter(commentAdapter);
        commentText = findViewById(R.id.commentText);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        Switch locationSwitch = findViewById(R.id.switchLocation);
        Button postButton = findViewById(R.id.btnPost);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (preLatitude != null && location != null) {
                    float[] res = new float[3];
                    Location.distanceBetween(preLatitude, preLongitude, location.getLatitude(), location.getLongitude(), res);
                }
                if (location != null) {
                    preLatitude = location.getLatitude();
                    preLongitude = location.getLongitude();
                    try {
                        Geocoder geo = new Geocoder(CommentActivity.this, Locale.getDefault());
                        List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.isEmpty()) {
                            locationSwitch.setText("Waiting for Location");
                        } else {
                            if (addresses.size() > 0) {
                                country = addresses.get(0).getCountryName();
                                if(switchOn){
                                    locationSwitch.setText(country);
                                } else {
                                    locationSwitch.setText("Show where I am");
                                }
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(CommentActivity.this, "Error fetching location info.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentTextVisibility = !commentTextVisibility;
                if (commentTextVisibility) {
                    commentText.setVisibility(View.VISIBLE);
                    locationSwitch.setVisibility(View.VISIBLE);
                    postButton.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.delete));
                } else {
                    commentText.setVisibility(View.INVISIBLE);
                    locationSwitch.setVisibility(View.INVISIBLE);
                    postButton.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.add));
                }
            }
        });

        locationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priority = Priority.PRIORITY_HIGH_ACCURACY;
                switchOn = !switchOn;
                startUpdate();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentText.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this, "Please enter some text before submitting.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String currentNews = "news" + getCurrentNewsIndex();
                String newContent = commentText.getText().toString();
                String userName = getCurrentUserName();
                String timeStamp = getCurrentTimeStamp();
                String userLocation = switchOn ? country : "Unknown";

                Map<String, String> map = new HashMap<>();
                map.put("commentDate", timeStamp);
                map.put("content", newContent);
                map.put("location", userLocation);
                map.put("username", userName);

                databaseReference = FirebaseDatabase.getInstance().getReference("News");
                databaseReference.child(currentNews).child("comments").push().setValue(map);
                commentTextVisibility = false;
                commentList.add(new Comment(userName, newContent, timeStamp, userLocation));
                getIntent().putExtra("commentList", (ArrayList) commentList);
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void startUpdate() {
        createLocationRequests();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
          @Override
          public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
              isUpdateLocation = true;
              resetLocationUpdates();
          }
        });
    }

    private void createLocationRequests() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(priority);
    }

    private void resetLocationUpdates() {
        if (priority == Priority.PRIORITY_HIGH_ACCURACY) {
            if (checkLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } else {
                requestLocationPermission();
            }
        } else {
            if (checkLocationPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } else {
                requestPermission();
            }
        }
    }

    public boolean checkLocationPermission(String permission) {
        boolean isPermitted = false;
        if (ContextCompat.checkSelfPermission(CommentActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
            isPermitted = true;
        }
        return isPermitted;
    }

    public void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this).setCancelable(false).setMessage("Permission Needed")
                    .setPositiveButton("Yes", (dialog, which) -> {ActivityCompat.requestPermissions(CommentActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 99);})
                    .setNegativeButton("No", ((dialog, which) -> dialog.cancel())).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
        }
    }

    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            new AlertDialog.Builder(this).setCancelable(false).setMessage("Permission Needed")
                    .setPositiveButton("Yes", (dialog, which) -> {ActivityCompat.requestPermissions(CommentActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);})
                    .setNegativeButton("No", ((dialog, which) -> dialog.cancel())).create().show();
        } else {
            ActivityCompat.requestPermissions(CommentActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 99) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
                startUpdate();
            } else if (grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults.length > 1) {
                new AlertDialog.Builder(this).setCancelable(false).setMessage("Permission Needed").setPositiveButton("Yes", (dialog, which) -> dialog.cancel()).create().show();
                priority = Priority.PRIORITY_HIGH_ACCURACY;
                startUpdate();
            } else {
                isUpdateLocation = false;
            }
        } else if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
                startUpdate();
            } else {
                isUpdateLocation = false;
            }
        }
    }

    private String getCurrentUserName(){
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        return sp.getString("name","");
    }

    private String getCurrentTimeStamp(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    private String getCurrentNewsIndex(){
        SharedPreferences sp = getSharedPreferences("newsInfo", MODE_PRIVATE);
        return sp.getString("newsIndex","");
    }

    private String getCurrentCommentIndex(){
        SharedPreferences sp = getSharedPreferences("newsInfo", MODE_PRIVATE);
        return sp.getString("commentIndex","");
    }

}
