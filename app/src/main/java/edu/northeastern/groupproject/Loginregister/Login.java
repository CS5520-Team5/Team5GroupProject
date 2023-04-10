package edu.northeastern.groupproject.Loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.groupproject.MainActivity;
import edu.northeastern.groupproject.R;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://team5-9cb36-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);
        mAuth = FirebaseAuth.getInstance();

        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                if(phoneTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(Login.this, "Please enter your mobile and password", Toast.LENGTH_SHORT).show();
                }
                else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if mobile/phone is exist in firebase database
                            if(snapshot.hasChild(phoneTxt)){
                                //mobile is exist in firebase database
                                //now get password of user from firebase data and match it with user entered password
                                final String getPassword = snapshot.child(phoneTxt).child("password").getValue(String.class);
                                final String userName = snapshot.child(phoneTxt).child("fullname").getValue(String.class);

                                if(getPassword.equals(passwordTxt)){
                                    Toast.makeText(Login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();

                                    //open MainActivity on success
                                    SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                    sharedPreferences.edit().putString("name", userName).apply();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
//                                    signInWithPhoneNumberAndPassword(userName, getPassword);
                                }
                                else{
                                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Login.this, "Wrong Mobile Number", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open Register activity
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}