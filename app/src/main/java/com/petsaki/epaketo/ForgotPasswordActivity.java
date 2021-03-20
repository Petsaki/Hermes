package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailText;
    private TextInputLayout emailtextInput;
    private ProgressBar progressBar;
    private Toast toast;

    private long lastTimeSent,spamTime = 0;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailText = (EditText) findViewById(R.id.forgot_email_id);
        emailtextInput = (TextInputLayout) findViewById(R.id.forgot_text_input_email_id);
        progressBar=(ProgressBar)findViewById(R.id.forgot_progressBar);
        auth=FirebaseAuth.getInstance();

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

    public void reset_password_click(View view){
        String email = emailText.getText().toString().trim();

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

        if(System.currentTimeMillis() > lastTimeSent + 60000) {
            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showToast("Email sended!");
                        lastTimeSent = System.currentTimeMillis();
                    } else {
                        showToast("Something went wrong.");
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });

        }else{
            showToast("You must wait 1 minute for a new one!");

        }
    }

    public void showToast(String string) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(ForgotPasswordActivity.this, string, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}