package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.northeastern.groupproject.R;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ImageButton btnEdit;
    private TextView phoneText;
    private TextView nameText;
    private TextView ageText;
    private TextView emailText;
    private TextView gamesText;
    private ImageView profileImage;
    private SharedPreferences sharedPreferences;
    private String name;
    private String age;
    private String email;
    private String games;
    private String phone;
    private String avatarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnEdit = findViewById(R.id.editButton);
        nameText = findViewById(R.id.profileName);
        phoneText = findViewById(R.id.profilePhone);
        ageText = findViewById(R.id.profileAge);
        emailText = findViewById(R.id.profileEmail);
        gamesText = findViewById(R.id.profileGames);
        profileImage = findViewById(R.id.profileImage);
        // Read user info
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        // Update user's name and phone number
        name = sharedPreferences.getString("name", "");
        nameText.setText(name);
        phone = sharedPreferences.getString("phone", "");
        String phoneString = "Phone: " + phone;
        phoneText.setText(phoneString);

        //Update user profile information
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(phone).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    showToast("Failed to get profile information");
                } else {
                    // Register a listener to read data from newest update in database
                    ValueEventListener profileListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            name = snapshot.child("fullname").getValue(String.class);
                            nameText.setText(name);
                            email = snapshot.child("email").getValue(String.class);
                            String emailString = "Email: " + email;
                            emailText.setText(emailString);
                            age = snapshot.child("age").getValue(String.class);
                            String ageString = "Age: " + age;
                            ageText.setText(ageString);
                            games = snapshot.child("games").getValue(String.class);
                            String gamesString = "Favorite Games: " + games;
                            gamesText.setText(gamesString);
                            avatarUri = snapshot.child("avatar").getValue(String.class);
                            setImage(avatarUri);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            showToast("Failed to get updated profile information");
                        }
                    };
                    databaseReference.child(phone).addValueEventListener(profileListener);
                }
            }
        });

        // Register onClickListener to edit button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                bundle.putString("name", name);
                bundle.putString("age", age);
                bundle.putString("email", email);
                bundle.putString("games", games);
                bundle.putString("avatar", avatarUri);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    // Display a toast with message
    private void showToast(String msg) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Set and display image from its URI
    private void setImage(String uri) {
        RequestOptions options = new RequestOptions()
                .override(100, 100)
                .centerCrop()
                .dontAnimate()
                .transform(new RoundedCorners(30))
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                ;
        Glide.with(getApplicationContext())
                .load(uri)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(profileImage);
    }
}