package edu.northeastern.groupproject.GameSphere;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.groupproject.R;

public class EditProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Button updateButton;
    private Button updateImageButton;
    private EditText editName;
    private EditText editAge;
    private EditText editEmail;
    private EditText editGames;
    private ImageView profileImage;
    private String fullname;
    private String age;
    private String email;
    private String games;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        updateButton = findViewById(R.id.editUpdateButton);
        updateImageButton = findViewById(R.id.editUpdateImageButton);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editAge = findViewById(R.id.editAge);
        editGames = findViewById(R.id.editGames);

        // Get information from bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        phone = bundle.getString("phone");
        fullname = bundle.getString("name");
        email = bundle.getString("email");
        age = bundle.getString("age");
        games = bundle.getString("games");
        // Initialize default information
        editName.setText(fullname);
        editEmail.setText(email);
        editAge.setText(age);
        editGames.setText(games);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Register onClickListener to update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdated()) {
                    String currName = editName.getText().toString();
                    String currEmail = editEmail.getText().toString();
                    String currAge = editAge.getText().toString();
                    String currGames = editGames.getText().toString();
                    databaseReference.child("users").child(phone).child("fullname").setValue(currName);
                    databaseReference.child("users").child(phone).child("email").setValue(currEmail);
                    databaseReference.child("users").child(phone).child("age").setValue(currAge);
                    databaseReference.child("users").child(phone).child("games").setValue(currGames);
                    fullname = currName;
                    email = currEmail;
                    age = currAge;
                    games = currGames;
                    onBackPressed();
                } else {
                    showToast("Please provide new profile information");
                }
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
        if (isUpdated()) {
            showDialog();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isUpdated() {
        String currName = editName.getText().toString();
        String currEmail = editEmail.getText().toString();
        String currAge = editAge.getText().toString();
        String currGames = editGames.getText().toString();
        return !currName.equals(fullname) || !currEmail.equals(email)
                || !currAge.equals(age) || !currGames.equals(games);
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

    // Display a toast with message
    private void showToast(String msg) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}