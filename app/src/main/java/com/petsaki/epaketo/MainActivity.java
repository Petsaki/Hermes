package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText usernameText,passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Epaketo);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        progressBar=(ProgressBar)findViewById(R.id.act1_progressBar_id);

    }

    public void login_in_click(View view){
        usernameText=(EditText) findViewById(R.id.act1_usernane_id);
        passwordText=(EditText) findViewById(R.id.act1_password_id);

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (username.isEmpty()){
            usernameText.setError("*Username is required!");
            usernameText.requestFocus();
            return;
        }else{
            usernameText.setError(null);
        }

        if (password.isEmpty()){
            passwordText.setError("*Password is required!");
            passwordText.requestFocus();
            return;
        }else{
            passwordText.setError(null);
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                    //if(user.isEmailVerified()){
                        Intent intent = new Intent( MainActivity.this, ConnectedActivity.class);
                        startActivity(intent);
                   // }else{
                    //    user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    //}
                    
                }else{
                    Toast.makeText(MainActivity.this,"Failed to login!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void sing_up_click(View view){
        Intent intent = new Intent( this, SingUpActivity.class);
        startActivity(intent);
    }

    public void forgot_password_click(View view){
        Intent intent = new Intent( this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

}