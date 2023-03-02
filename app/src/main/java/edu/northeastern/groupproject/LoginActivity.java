package edu.northeastern.groupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText editText;
    private Button loginButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.editTxt_login);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String name = editText.getText().toString();
        // Check if the username is empty
        if (name.length() != 0) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(name);
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Get user information
                        User user = task.getResult().getValue(User.class);
                        // Check if user exists
                        if (user != null && user.name != null && user.name.length() > 0) {
                            // Save username
                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            sharedPreferences.edit().putString("name", name).apply();
                            // Start new activity
                            Intent intent = new Intent(LoginActivity.this, StickerActivity.class);
                            startActivity(intent);
                        } else {
                            // Display a toast if user not exists
                            String notExistsMsg = "User not exists!";
                            showToast(notExistsMsg);
                        }
                    }
                }
            });
        } else {
            // Display a toast if the username is empty
            String emptyMsg = "Username can not be empty!";
            showToast(emptyMsg);
        }
    }

    // Display a toast with message
    private void showToast(String msg) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
