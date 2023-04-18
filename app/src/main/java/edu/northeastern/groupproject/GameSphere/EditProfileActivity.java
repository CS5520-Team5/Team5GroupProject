package edu.northeastern.groupproject.GameSphere;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import edu.northeastern.groupproject.R;

public class EditProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Button updateButton;
    private Button uploadImageButton;
    private EditText editName;
    private EditText editAge;
    private EditText editEmail;
    private EditText editGames;
    private ImageView editImage;
    private String fullname;
    private String age;
    private String email;
    private String games;
    private String phone;
    private String avatarUri;
    private String currAvatarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        updateButton = findViewById(R.id.editUpdateButton);
        uploadImageButton = findViewById(R.id.editUpdateImageButton);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editAge = findViewById(R.id.editAge);
        editGames = findViewById(R.id.editGames);
        editImage = findViewById(R.id.editImage);

        // Get information from bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        phone = bundle.getString("phone");
        fullname = bundle.getString("name");
        email = bundle.getString("email");
        age = bundle.getString("age");
        games = bundle.getString("games");
        avatarUri = bundle.getString("avatar");
        currAvatarUri = avatarUri;

        // Initialize default information
        editName.setText(fullname);
        editEmail.setText(email);
        editAge.setText(age);
        editGames.setText(games);
        setImage(avatarUri);

        // initialize database and storage references
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("AvatarImage/");

        // Register onClickListener to update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdated()) {
                    String currName = editName.getText().toString();
                    String currEmail = editEmail.getText().toString();
                    String currAge = editAge.getText().toString();
                    String currGames = editGames.getText().toString();
                    databaseReference.child(phone).child("fullname").setValue(currName);
                    databaseReference.child(phone).child("email").setValue(currEmail);
                    databaseReference.child(phone).child("age").setValue(currAge);
                    databaseReference.child(phone).child("games").setValue(currGames);
                    databaseReference.child(phone).child("avatar").setValue(currAvatarUri);
                    // Update variables
                    fullname = currName;
                    email = currEmail;
                    age = currAge;
                    games = currGames;
                    avatarUri = currAvatarUri;
                    onBackPressed();
                } else {
                    showToast("Please provide updated information");
                }
            }
        });

        // Register onClickListener to update image button
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imageChooser.launch(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isUpdated()) {
            showDialog();
        } else {
            super.onBackPressed();
        }
    }

    // Check if profile information has been updated
    private boolean isUpdated() {
        String currName = editName.getText().toString();
        String currEmail = editEmail.getText().toString();
        String currAge = editAge.getText().toString();
        String currGames = editGames.getText().toString();
        return !currName.equals(fullname) || !currEmail.equals(email)
                || !currAge.equals(age) || !currGames.equals(games)
                || !currAvatarUri.equals(avatarUri);
    }

    // Show a dialog asking the user if they want to terminate the search
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the message in the dialog
        builder.setMessage("Do you want to go back without saving?");
        // Set the YES button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditProfileActivity.super.onBackPressed();
            }
        });
        // Set the NO button
        builder.setNegativeButton("No", null);
        // Show the dialog
        builder.show();
    }

    // Display a toast with message
    private void showToast(String msg) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Upload image to Firebase storage using its URI
    private void uploadImage(Uri uri) {
        // Get storage reference
        StorageReference imageStorageReference = storageReference.child(uri.getLastPathSegment());
        // Upload image
        UploadTask uploadTask = imageStorageReference.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Update avatar image URI in user profile
                currAvatarUri = uri.toString();
                setImage(currAvatarUri);
            }
        });
    }

    // Create an instance of image chooser
    private ActivityResultLauncher<Intent> imageChooser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // Get image URI
                        Uri uri = result.getData().getData();
                        // Upload image
                        uploadImage(uri);
                    }
                }
            }
    );

    // Set and display image from its URI
    private void setImage(String uri) {
        RequestOptions options = new RequestOptions()
                .override(100, 100)
                .centerCrop()
                .dontAnimate()
                .transform(new RoundedCorners(30))
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(getApplicationContext())
                .load(uri)
                .apply(options)
                .into(editImage);
    }
}