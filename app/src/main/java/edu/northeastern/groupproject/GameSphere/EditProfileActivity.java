package edu.northeastern.groupproject.GameSphere;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

import edu.northeastern.groupproject.R;

public class EditProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Button updateButton;
    private Button cancelButton;
    private Button updateImageButton;
    private EditText editUsername;
    private EditText editAge;
    private EditText editEmail;
    private EditText editGames;
    private ImageView profileImage;
    private String username;
    private String age;
    private String email;
    private String games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        updateButton = findViewById(R.id.editUpdateButton);
        cancelButton = findViewById(R.id.editCancelButton);
        updateImageButton = findViewById(R.id.editUpdateImageButton);
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editAge = findViewById(R.id.editAge);
        editGames = findViewById(R.id.editGames);

        // Get information from bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        username = bundle.getString("username");
        email = bundle.getString("email");
        age = bundle.getString("age");
        games = bundle.getString("games");
        // Initialize default information
        editUsername.setText(username);
        editEmail.setText(email);
        editAge.setText(age);
        editGames.setText(games);

        // Register onClickListener to update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Register onClickListener to cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        // Register onClickListener to update image button
        // TODO: Implement updating profile image
        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    // Show a dialog asking the user if they want to terminate the search
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the message in the dialog
        builder.setMessage("Do you want to go back without saving?");
        // Set the YES button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditProfileActivity.super.onBackPressed();
            }
        });
        // Set the NO button
        builder.setNegativeButton("No", null);
        // Show the dialog
        builder.show();
    }
}