package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectedActivity extends AppCompatActivity {

    private String userID;
    private DatabaseReference reference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);


        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();

        final TextView usernametext=(TextView)findViewById(R.id.text_username);
        final TextView emailtext=(TextView)findViewById(R.id.text_email);
        TextView passwordtext=(TextView)findViewById(R.id.textView3);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userInfo= snapshot.getValue(User.class);

                if(userInfo != null){
                    String username= userInfo.username;
                    String email=userInfo.email;

                    usernametext.setText(username);
                    emailtext.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConnectedActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout_click(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent( ConnectedActivity.this, MainActivity.class);
        startActivity(intent);
    }
}