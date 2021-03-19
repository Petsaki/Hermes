package com.petsaki.epaketo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SingUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        //Metablhtes antikimenwn
        TextInputEditText usernameText = (TextInputEditText) findViewById(R.id.act2_username_id);
        EditText passwordText = (EditText) findViewById(R.id.act2_password_id);
        EditText emailText = (EditText) findViewById(R.id.act2_email_id);

        TextInputLayout usernametextInput = (TextInputLayout) findViewById(R.id.act2_text_input_username_id);
        TextInputLayout passwordtextInput = (TextInputLayout) findViewById(R.id.act2_text_input_password_id);
        TextInputLayout emailtextInput = (TextInputLayout) findViewById(R.id.act2_text_input_email_id);

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
        TextInputEditText usernameText = (TextInputEditText) findViewById(R.id.act2_username_id);
        EditText passwordText = (EditText) findViewById(R.id.act2_password_id);
        EditText emailText = (EditText) findViewById(R.id.act2_email_id);

        TextInputLayout usernametextInput = (TextInputLayout) findViewById(R.id.act2_text_input_username_id);
        TextInputLayout passwordtextInput = (TextInputLayout) findViewById(R.id.act2_text_input_password_id);
        TextInputLayout emailtextInput = (TextInputLayout) findViewById(R.id.act2_text_input_email_id);

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String email = emailText.getText().toString().trim();

        if (username.length()==0) {
            usernametextInput.setError("Can't be empty");
        } else {
            usernametextInput.setError(null);
        }

        if (password.length()==0) {
            passwordtextInput.setError("Can't be empty");
        } else if (password.length() < 8) {
            passwordtextInput.setError("Password must be at least 8 letters");
        } else {
            passwordtextInput.setError(null);
        }

        if (email.length()==0){
            emailtextInput.setError("Can't be empty");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtextInput.setError("Wrong mail pattern!");
        }else{
            emailtextInput.setError(null);
        }

    }

}