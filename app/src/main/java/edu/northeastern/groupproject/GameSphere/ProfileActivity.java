package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.groupproject.R;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ImageButton btnEdit;
    private TextView usernameText;
    private TextView nameText;
    private TextView ageText;
    private TextView emailText;
    private TextView gamesText;
    private ImageView profileImage;
    private SharedPreferences sharedPreferences;
    private String username;
    private String name;
    private String age;
    private String email;
    private String games;
    private Integer id;

    // TODO: Implement profile image display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnEdit = (ImageButton) findViewById(R.id.editButton);
        usernameText = findViewById(R.id.profileUsername);
        nameText = findViewById(R.id.profileName);
        ageText = findViewById(R.id.profileAge);
        emailText = findViewById(R.id.profileEmail);
        gamesText = findViewById(R.id.profileGames);
        profileImage = findViewById(R.id.profileImage);
        // Read user info
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        // Update username
        username = sharedPreferences.getString("name", "");
        usernameText.setText(username);

        //Update user profile information
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    showToast("Failed to get profile information");
                } else {
                    id = task.getResult().child("id").getValue(Integer.class);
                    // Register a listener to read data from newest update in database
                    ValueEventListener profileListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            name = snapshot.child("name").getValue(String.class);
                            String nameString = "Name: " + name;
                            nameText.setText(nameString);
                            email = snapshot.child("email").getValue(String.class);
                            String emailString = "Email: " + email;
                            emailText.setText(emailString);
                            age = snapshot.child("age").getValue(String.class);
                            String ageString = "Age: " + age;
                            ageText.setText(ageString);
                            games = snapshot.child("games").getValue(String.class);
                            String gamesString = "Games: " + games;
                            gamesText.setText(gamesString);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            showToast("Failed to get updated profile information");
                        }
                    };
                    databaseReference.child(username).addValueEventListener(profileListener);
                }
            }
        });

        // Register onClickListener to edit button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("username", username);
                bundle.putString("name", name);
                bundle.putString("age", age);
                bundle.putString("email", email);
                bundle.putString("games", games);
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
}