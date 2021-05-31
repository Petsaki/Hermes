package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailText;
    private TextInputLayout emailtextInput;
    private ProgressBar progressBar;
    private Toast toast;
    private Button button;

    private long lastTimeSent,spamTime = 0;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailText = (EditText) findViewById(R.id.forgot_email_id);
        emailtextInput = (TextInputLayout) findViewById(R.id.forgot_text_input_email_id);
        progressBar=(ProgressBar)findViewById(R.id.forgot_progressBar);
        button=(Button) findViewById(R.id.forgot_button);
        auth=FirebaseAuth.getInstance();

        //To exw mono otan allackei kati na bgalei to error
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

        emailText.setOnEditorActionListener(editorListener);
    }

    //Otan grafei to email, anti gia to enter exw balei to send(to belaki) kai kalw thn function tou koumpiou apostolh
    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch(actionId){
                case EditorInfo
                        .IME_ACTION_SEND:
                    button.performClick();
                    break;
            }
            return true;
        }
    };

    public void reset_password_click(View view){
        String email = emailText.getText().toString().trim();

        //tsekarw gia lathoi
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



        //Stelnei to email + exw antispam alla to pernane eukola
        if(System.currentTimeMillis() > lastTimeSent + 60000) {
            progressBar.setVisibility(View.VISIBLE);
            button.setEnabled(false);
            closeKeyboard();
            button.setFocusable(true);
            button.setFocusableInTouchMode(true);
            button.requestFocus();
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        toast.makeText(ForgotPasswordActivity.this, "", Toast.LENGTH_LONG);
                        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                            showToast("Το email στάλθηκε!");
                        }else{
                            toast.cancel();
                        }
                        lastTimeSent = System.currentTimeMillis();
                    } else {
                        showToast("Κάτι πήγε στραβά.");
                    }
                    progressBar.setVisibility(View.GONE);
                    button.setEnabled(true);
                }
            });

        }else{
            showToast("Πρέπει να περιμένεις 1 λεπτό για καινούργιο email!");

        }
    }

    public void showToast(String string) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(ForgotPasswordActivity.this, string, Toast.LENGTH_LONG);
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
        Intent intent = new Intent(this, getClass());
        this.overridePendingTransition(R.anim.corner_down_right,R.anim.slide_out_right);
        finish();
    }
}