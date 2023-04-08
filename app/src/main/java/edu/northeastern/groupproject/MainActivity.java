package edu.northeastern.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.northeastern.groupproject.FirebaseAuth.Login;
import edu.northeastern.groupproject.GameSphere.GameSphereLoginActivity;
import edu.northeastern.groupproject.GameSphere.RoomActivity;
import edu.northeastern.groupproject.Sticker.LoginActivity;
import edu.northeastern.groupproject.WebService.WebServiceActivity;
import edu.northeastern.groupproject.GameSphere.Home;

public class MainActivity extends AppCompatActivity {

    Button btnWebService, btnAboutUs, btnSendASticker, btnGameSphere,btnGameHome;

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
                Intent intent = new Intent(MainActivity.this, GameSphereLoginActivity.class);
                startActivity(intent);
            }
        });


        btnGameHome = findViewById(R.id.btnGameHome);
        btnGameHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked bypass");
                Log.i("MyApp","clicked bypass");
                Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                startActivity(intent);
            }
        });



        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            textView.setText(user.getEmail());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}