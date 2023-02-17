package edu.northeastern.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
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
    private CheckBox cbNsfw, cbReligious, cbPolitical, cbRacist;
    private boolean noNsfw, noReligious, noPolitical, noRacist;
    private Handler handler = new Handler();
    CounterThread counterThread;
    String result = new String();
    String basicURL = "https://v2.jokeapi.dev/joke/Any";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        cardView = findViewById(R.id.cardGenerateJoke);
        textView = findViewById(R.id.testText);
        cbNsfw = findViewById(R.id.checkbox1);
        cbReligious = findViewById(R.id.checkbox2);
        cbPolitical = findViewById(R.id.checkbox3);
        cbRacist = findViewById(R.id.checkbox4);
        responseTimeText = findViewById(R.id.responseTimeText);
        cbNsfw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noNsfw = true;
            }
        });
        cbReligious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noReligious = true;
            }
        });
        cbPolitical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noPolitical = true;
            }
        });
        cbRacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noRacist = true;
            }
        });
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
        @Override
        public void run() {
            URL url;
            try {
                addBlacklistToURL();
                url = new URL(basicURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                JSONObject jsonObject = new JSONObject(inputStreamToString(inputStream));
                result = jsonObject.get("joke").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(result);
                    counterThread.interrupt();
                }
            });
        }

        private String inputStreamToString(InputStream is) {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next().replace(",", ",\n") : "";
        }
    }

    private void addBlacklistToURL() {
        if (noNsfw || noPolitical || noRacist || noReligious) {
            basicURL += "?blacklistFlags=";
        } else {
            basicURL += "?type=single";
            return;
        }

        if (noNsfw) {
            basicURL += "nsfw";
        }
        if (noReligious) {
            basicURL += (basicURL.charAt(basicURL.length() - 1) == '=' ? "" : ",");
            basicURL += "religious";
        }
        if (noPolitical) {
            basicURL += (basicURL.charAt(basicURL.length() - 1) == '=' ? "" : ",");
            basicURL += "political";
        }
        if (noRacist) {
            basicURL += (basicURL.charAt(basicURL.length() - 1) == '=' ? "" : ",");
            basicURL += "racist";
        }

        basicURL += "&type=single";
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