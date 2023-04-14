package edu.northeastern.groupproject.GameSphere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import edu.northeastern.groupproject.MainActivity;
import edu.northeastern.groupproject.R;
import edu.northeastern.groupproject.WebService.WebServiceActivity;

public class Home extends AppCompatActivity {

    Button btnNews,btnRooms;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_home);

        btnNews = findViewById(R.id.btnNews);
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        btnRooms = findViewById(R.id.btnRooms);
        btnRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, RoomActivity.class);
                startActivity(intent);
            }
        });
    }
}
