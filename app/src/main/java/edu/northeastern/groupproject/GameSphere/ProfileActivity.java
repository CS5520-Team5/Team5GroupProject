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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.groupproject.R;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ImageButton btnEdit;
    private TextView usernameText;
    private TextView ageText;
    private TextView emailText;
    private TextView gamesText;
    private ImageView profileImage;
    private SharedPreferences sharedPreferences;
    private String username;
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
        ageText = findViewById(R.id.profileAge);
        emailText = findViewById(R.id.profileEmail);
        gamesText = findViewById(R.id.profileGames);
        profileImage = findViewById(R.id.profileImage);
        // Read user info
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        // Update username
        username = sharedPreferences.getString("name", "");
        String usernameString = "Username: " + username;
        usernameText.setText(usernameString);

        //Update user profile information
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(username);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    showToast("Failed to get profile information");
                } else {
                    id = Integer.valueOf(getInfo(task, "id"));
                    age = getInfo(task, "age");
                    String ageString = "Age: " + age;
                    ageText.setText(ageString);
                    email = getInfo(task, "email");
                    String genderString = "Email: " + email;
                    emailText.setText(genderString);
                    games = getInfo(task, "games");
                    String gamesString = "Games: " + games;
                    gamesText.setText(gamesString);
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
                bundle.putString("age", age);
                bundle.putString("email", email);
                bundle.putString("games", games);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    // Get profile information
    private String getInfo(Task<DataSnapshot> task, String fieldName) {
        String result = "";
        Object infoObject = task.getResult().child(fieldName).getValue();
        // Check if infoObject contains this field
        if (infoObject != null) {
            result = result + infoObject;
        }
        return result;
    }

    // Display a toast with message
    private void showToast(String msg) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}