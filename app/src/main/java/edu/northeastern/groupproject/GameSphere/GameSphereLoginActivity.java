package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import edu.northeastern.groupproject.R;
import edu.northeastern.groupproject.Sticker.User;

public class GameSphereLoginActivity extends AppCompatActivity {
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
        String loginName = editText.getText().toString();
        if (loginName.length() != 0) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(loginName);
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = task.getResult().getValue(User.class);
                        if (!(user != null && user.getName() != null && user.getName().length() > 0)) {
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    long count = dataSnapshot.getChildrenCount();
                                    User newUser = new User();
                                    newUser.setId((int) count + 100);
                                    newUser.setName(loginName);
                                    databaseReference.setValue(newUser);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    showToast("Something went wrong, please try again.");
                                }

                            };
                            DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("User");
                            d.addListenerForSingleValueEvent(valueEventListener);
                        }
                        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                        sharedPreferences.edit().putString("name", loginName).apply();
                        Intent intent = new Intent(GameSphereLoginActivity.this, Home.class);
                        startActivity(intent);
                    }
                }
            });
        } else {
            showToast("Username can not be empty!");
        }
    }

    private void showToast(String msg) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}