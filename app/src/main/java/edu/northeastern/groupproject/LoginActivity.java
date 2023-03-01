package edu.northeastern.groupproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText editText;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.editTxt_login);
        progressBar = findViewById(R.id.loadingCircle);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String name = editText.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            progressBar.setVisibility(View.VISIBLE);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(name);
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = task.getResult().getValue(User.class);
                        if (user != null && !TextUtils.isEmpty(user.name)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            sharedPreferences.edit().putString("name", name).apply();
                            // To Do
                            // startActivity(new Intent(MainActivity.this, XXXXXXXXX));
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
