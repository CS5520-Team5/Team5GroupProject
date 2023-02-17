package edu.northeastern.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WebServiceActivity extends AppCompatActivity {

    private CardView cardView;
    private TextView textView;
    private TextView responseTimeText;
    private Handler handler = new Handler();
    CounterThread counterThread;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        cardView = findViewById(R.id.cardGenerateJoke);
        textView = findViewById(R.id.testText);
        responseTimeText = findViewById(R.id.responseTimeText);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonRunnable jsonRunnable = new JsonRunnable();
                new Thread(jsonRunnable).start();
                counterThread = new CounterThread();
                counterThread.start();
            }
        });
    }


    class JsonRunnable implements Runnable{
        StringBuilder sb = new StringBuilder();
        @Override
        public void run() {
            URL url;
            String ndcURL = "https://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid=814380&count=3&maxlength=1000&format=json";

            try {
                url = new URL(ndcURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                JSONObject jsonObject = new JSONObject(inputStreamToString(inputStream));
                JSONArray jsonArray = jsonObject.getJSONObject("appnews").getJSONArray("newsitems");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    sb.append( i + 1 + "\t" + obj.getString("title") + "\n\n\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(sb.toString());
                    counterThread.interrupt();
                }
            });
        }

        private String inputStreamToString(InputStream is) {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next().replace(",", ",\n") : "";
        }
    }

    // Inner class to display a counter for API response time
    class CounterThread extends Thread {

        private int n = 0;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && n < Integer.MAX_VALUE) {
                handler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            String text = "Response time: " + n + " ms";
                            responseTimeText.setText(text);
                        }
                    }
                );
                try {
                    // Increment in every millisecond
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                // Increment in every millisecond
                n += 1;
            }
        }
    }
}