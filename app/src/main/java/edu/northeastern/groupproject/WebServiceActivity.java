package edu.northeastern.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class WebServiceActivity extends AppCompatActivity {

    private TextView responseTimeText;
    private CheckBox cbNsfw, cbReligious, cbPolitical, cbRacist;
    private boolean noNsfw, noReligious, noPolitical, noRacist;
    private Button generateButton;
    private Handler handler = new Handler();
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private CounterThread counterThread;
    private ArrayList<Joke> jokes;
    private String basicURL = "https://v2.jokeapi.dev/joke/Any?type=twopart&amount=10";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);
        generateButton = findViewById(R.id.generateButton);
        cbNsfw = findViewById(R.id.checkbox1);
        cbReligious = findViewById(R.id.checkbox2);
        cbPolitical = findViewById(R.id.checkbox3);
        cbRacist = findViewById(R.id.checkbox4);
        responseTimeText = findViewById(R.id.responseTimeText);

        if(savedInstanceState!=null){
            jokes=savedInstanceState.getParcelableArrayList("jokes");
        }else{
            jokes=new ArrayList<Joke>();
        }

        cbNsfw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noNsfw = !noNsfw;
            }
        });
        cbReligious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noReligious = !noReligious;
            }
        });
        cbPolitical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noPolitical = !noPolitical;
            }
        });
        cbRacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noRacist = !noRacist;
            }
        });
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jokes.clear();
                recyclerAdapter.notifyDataSetChanged();
                JsonRunnable jsonRunnable = new JsonRunnable();
                new Thread(jsonRunnable).start();
                counterThread = new CounterThread();
                counterThread.start();
            }
        });

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter=new RecyclerAdapter(jokes,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("jokes", (ArrayList<? extends Parcelable>) jokes);
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
                parseInputToJokes(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    counterThread.interrupt();
                }
            });
        }

        private String inputStreamToString(InputStream is) {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next().replace(",", ",\n") : "";
        }
    }
    private void parseInputToJokes(JSONObject jsonObject) throws JSONException {
        // example output: https://v2.jokeapi.dev/joke/Any?type=twopart&amount=5&blacklistFlags=
        JSONArray jsonArray=jsonObject.getJSONArray("jokes");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject joke = jsonArray.getJSONObject(i);
            String category=joke.get("category").toString();
            String setup=joke.get("setup").toString();
            String delivery=joke.get("delivery").toString();
            Joke newjoke=new Joke(category,setup,delivery);
            jokes.add(newjoke);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    private void addBlacklistToURL() {
        ArrayList<String> blacklistFlags=new ArrayList<>();
        if(noNsfw){
            blacklistFlags.add("nsfw");
        }else if(noReligious){
            blacklistFlags.add("religious");
        }else if(noPolitical){
            blacklistFlags.add("political");
        }else if(noRacist){
            blacklistFlags.add("racist");
        }
        String blacklistStr=String.join(",",blacklistFlags);
        basicURL+="&&blacklistFlags="+blacklistStr;
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