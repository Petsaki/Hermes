package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SingUpActivity extends AppCompatActivity {

    private TextInputEditText usernameText;
    private EditText passwordText, emailText;
    private TextInputLayout usernametextInput, passwordtextInput, emailtextInput;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();

        //Metablhtes antikimenwn
        usernameText = (TextInputEditText) findViewById(R.id.act2_username_id);
        passwordText = (EditText) findViewById(R.id.act2_password_id);
        emailText = (EditText) findViewById(R.id.act2_email_id);

        usernametextInput = (TextInputLayout) findViewById(R.id.act2_text_input_username_id);
        passwordtextInput = (TextInputLayout) findViewById(R.id.act2_text_input_password_id);
        emailtextInput = (TextInputLayout) findViewById(R.id.forgot_text_input_email_id);

        progressBar=(ProgressBar)findViewById(R.id.act2_progressBar_id);

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    usernametextInput.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    passwordtextInput.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    emailtextInput.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void create_account_click(View view) {

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String email = emailText.getText().toString().trim();

        if (username.isEmpty()) {
            usernametextInput.setError("Can't be empty");
            usernametextInput.requestFocus();
            return;
        } else {
            usernametextInput.setError(null);
        }

        if (password.isEmpty()) {
            passwordtextInput.setError("Can't be empty");
            passwordtextInput.requestFocus();
            return;
        } else if (password.length() < 8) {
            passwordtextInput.setError("Password must be at least 8 letters");
            passwordtextInput.requestFocus();
            return;
        } else {
            passwordtextInput.setError(null);
        }

        if (email.isEmpty()){
            emailtextInput.setError("Can't be empty");
            emailtextInput.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtextInput.setError("Wrong mail pattern!");
            emailtextInput.requestFocus();
            return;
        }else{
            emailtextInput.setError(null);
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SingUpActivity.this,new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if(task.isSuccessful()){
                                                   User user = new User(username,email);
                                                   FirebaseDatabase.getInstance().getReference("Users")
                                                           .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                           .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if(task.isSuccessful()){
                                                               Toast.makeText(SingUpActivity.this, "User has been singed up successfully!",Toast.LENGTH_LONG).show();
                                                           }else{
                                                               Toast.makeText(SingUpActivity.this, "FAILED to singed up. Try again!", Toast.LENGTH_LONG).show();
                                                           }
                                                           progressBar.setVisibility(View.GONE);
                                                       }
                                                   });
                                               }else {
                                                   Toast.makeText(SingUpActivity.this, "Failed to singed up. Try again!", Toast.LENGTH_LONG).show();
                                                   progressBar.setVisibility(View.GONE);
                                               }
                                           }
                                       });
/*
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SingUpActivity.this, task -> {
                    if (task.isSuccessful()){
                        User user= new User(username,email);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()){
                                        Toast.makeText(SingUpActivity.this, "User has been singed up successfully!",Toast.LENGTH_LONG).show();

                                    }else{
                                        Toast.makeText(SingUpActivity.this, "FAILED to singed up. Try again!", Toast.LENGTH_LONG).show();
                                    }
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SingUpActivity.this, "PERASA",Toast.LENGTH_LONG).show();
                            });
                    }else{
                        Toast.makeText(SingUpActivity.this, "Failed to singed up. Try again!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });*/

    }

}