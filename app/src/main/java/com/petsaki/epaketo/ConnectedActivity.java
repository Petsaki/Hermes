package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import static com.petsaki.epaketo.SaveSharedPreference.clearUserName;

public class ConnectedActivity extends AppCompatActivity {

    private String userID;
    private DatabaseReference reference;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private String loginMethod;
    private AuthUI authUI;
    //private String username,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Epaketo);
        setContentView(R.layout.activity_connected);

        mAuth=FirebaseAuth.getInstance();

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();

        final TextView usernametext=(TextView)findViewById(R.id.text_username);
        final TextView emailtext=(TextView)findViewById(R.id.text_email);

        for (UserInfo user: Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getProviderData()) {

            if (user.getProviderId().equals("password")) {
                loginMethod=user.getProviderId();
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userInfo = snapshot.getValue(User.class);

                        if (userInfo != null) {
                            String username = userInfo.username;
                            String email = userInfo.email;
                            usernametext.setText(username);
                            emailtext.setText(email);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ConnectedActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                });

            } else if (user.getProviderId().equals("google.com")) {
                loginMethod=user.getProviderId();
                if (user != null) {
                    String username = user.getDisplayName();
                    String email = user.getEmail();
                    usernametext.setText(username);
                    emailtext.setText(email);
                }
            }
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }







    }

    public void logout_click(View view){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
        Intent intent = new Intent( ConnectedActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        //Einai pio swstos tropos alla den douleuei gia kapoio logo
       /* AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ConnectedActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent( ConnectedActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });*/
    }
}