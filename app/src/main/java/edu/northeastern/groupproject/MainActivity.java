package edu.northeastern.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import edu.northeastern.groupproject.Loginregister.Login;
import edu.northeastern.groupproject.GameSphere.Home;
import edu.northeastern.groupproject.Sticker.LoginActivity;
import edu.northeastern.groupproject.WebService.WebServiceActivity;

public class MainActivity extends AppCompatActivity {

    Button btnWebService, btnAboutUs, btnSendASticker, btnGameSphere;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWebService = findViewById(R.id.btnWebService);
        btnWebService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebServiceActivity.class);
                startActivity(intent);
            }
        });

        btnAboutUs = findViewById(R.id.btnAboutUs);
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        btnSendASticker = findViewById(R.id.btnSendASticker);
        btnSendASticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnGameSphere = findViewById(R.id.btnGameSphere);
        btnGameSphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String getCurrentUserName(){
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        return sp.getString("name","");
    }
}