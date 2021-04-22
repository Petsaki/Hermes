package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SingUpActivity extends AppCompatActivity {

    private EditText usernameText,passwordText, emailText;
    private TextInputLayout usernametextInput, passwordtextInput, emailtextInput;
    private ProgressBar progressBar;
    private Toast toast;
    private Button button;

    private FirebaseAuth mAuth;

    private long lastTimeSent = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();

        usernameText = (EditText) findViewById(R.id.act2_username_id);
        emailText = (EditText) findViewById(R.id.act2_email_id);
        passwordText = (EditText) findViewById(R.id.act2_password_id);

        button=(Button)findViewById(R.id.act2_create_button_id);

        usernametextInput = (TextInputLayout) findViewById(R.id.act2_text_input_username_id);
        emailtextInput = (TextInputLayout) findViewById(R.id.forgot_text_input_email_id);
        passwordtextInput = (TextInputLayout) findViewById(R.id.act2_text_input_password_id);

        progressBar=(ProgressBar)findViewById(R.id.act2_progressBar_id);


        //Gia na bgei to eror otan kanei allagei sto text input
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

        //Gia na bgei to eror otan kanei allagei sto text input
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

        //Gia na bgei to eror otan kanei allagei sto text input
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


        passwordText.setOnEditorActionListener(editorActionListener);


    }

    //Allazei to enter sto keyboard se Send kai kali thn methodo apo ena button
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener(){
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch(actionId){
                case EditorInfo
                        .IME_ACTION_SEND:
                    button.performClick();
                    closeKeyboard();
                    break;
            }
            return true;
        }
    };

    public void create_account_click(View view) {

        String username = usernameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();


        if (username.isEmpty()) {
            usernametextInput.setError("Δεν μπορεί να είναι κενό");
            usernametextInput.requestFocus();
            return;
        } else {
            usernametextInput.setError(null);
        }

        if (email.isEmpty()){
            emailtextInput.setError("Δεν μπορεί να είναι κενό");
            emailtextInput.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtextInput.setError("Λάθος μοτίβο email!");
            emailtextInput.requestFocus();
            return;
        }else{
            emailtextInput.setError(null);
        }

        if (password.isEmpty()) {
            passwordtextInput.setError("Δεν μπορεί να είναι κενό");
            passwordtextInput.requestFocus();
            return;
        } else if (password.length() < 8) {
            passwordtextInput.setError("Πρέπει να είναι τουλάχιστον 8 χαρακτήρες");
            passwordtextInput.requestFocus();
            return;
        } else {
            passwordtextInput.setError(null);
        }


            closeKeyboard();
            button.setFocusable(true);
            button.setFocusableInTouchMode(true);
            button.requestFocus();

            progressBar.setVisibility(View.VISIBLE);
            button.setEnabled(false);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SingUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(username, email);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showToast("Δημιουργία λογαριασμού επιτυχής!");//+mAuth.getCurrentUser());
                                            FirebaseAuth.getInstance().signOut();
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    onBackPressed();
                                                }
                                            }, 1250);

                                        } else {
                                            showToast("Αποτυχία εγγραφής. Προσπάθησε ξανά!");
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                showToast("Αποτυχία εγγραφής. Προσπάθησε ξανά!");
                                progressBar.setVisibility(View.GONE);
                            }
                            button.setEnabled(true);
                        }

                    });


    }
    public void showToast(String string) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(SingUpActivity.this, string, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void closeKeyboard(){
        View view=this.getCurrentFocus();
        if (view !=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        this.overridePendingTransition(R.anim.corner_down_right,R.anim.slide_out_right);
        finish();
    }
}