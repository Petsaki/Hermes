package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText emailText,passwordText;
    private GoogleSignInClient mGoogleSignInClient;
    private long lastTimeSent,spamTime = 0;
    private Toast toast;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

        if (user!=null){
            Intent intent = new Intent( MainActivity.this, ConnectedActivity.class);
            startActivity(intent);
            finish();
        }
/*  SharedPreference isos to xreiastw parakatw, den to sbhnw

        if(SaveSharedPreference.getUserName(MainActivity.this).length() != 0)
        {
            Intent intent = new Intent( MainActivity.this, ConnectedActivity.class);

            startActivity(intent);
        }
     */
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Epaketo);
        setContentView(R.layout.activity_main);

        emailText = (EditText) findViewById(R.id.act1_email_id);
        passwordText = (EditText) findViewById(R.id.act1_password_id);
        button=(Button)findViewById(R.id.act1_login_id);

        progressBar=(ProgressBar)findViewById(R.id.act1_progressBar_id);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        passwordText.setOnEditorActionListener(editorListener);

    }

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

    public void login_in_click(View view) {


        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty()) {
            emailText.setError("*Email is required!");
            emailText.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Wrong mail pattern!");
            emailText.requestFocus();
            return;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("*Password is required!");
            passwordText.requestFocus();
            return;
        } else {
            passwordText.setError(null);
        }
        if (System.currentTimeMillis() > lastTimeSent + 30000) {
            closeKeyboard();
            button.setFocusable(true);
            button.setFocusableInTouchMode(true);
            button.requestFocus();
            progressBar.setVisibility(View.VISIBLE);
            button.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        //if(user.isEmailVerified()){
                        //setUserName(MainActivity.this,username);
                            Intent intent = new Intent(MainActivity.this, ConnectedActivity.class);
                            startActivity(intent);
                            finish();
                        //}else{
                        //user.sendEmailVerification();
                        showToast("Check your email to verify your account!");
                       // }
                        lastTimeSent = System.currentTimeMillis();
                    } else {
                        showToast("Failed to login!");
                    }
                    progressBar.setVisibility(View.GONE);
                    button.setEnabled(true);
                }
            });

        }else{
            showToast("You must wait 30 secs for a new verify email!");
        }
    }

    public void sing_up_click(View view){
        Intent intent = new Intent( this, SingUpActivity.class);
        startActivity(intent);
    }

    public void forgot_password_click(View view){
        Intent intent = new Intent( this, ForgotPasswordActivity.class);
        startActivity(intent);
    }


    public void google_sing_up_click(View view) {
        signIn();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //To Toast einai gia delete,tha to kanw argotera
                Toast.makeText(MainActivity.this,"firebaseAuthWithGoogle:" + account.getId(), Toast.LENGTH_LONG).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this,"Google sign in failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //To Toast einai gia delete,tha to kanw argotera
                            Toast.makeText(MainActivity.this,"signInWithCredential:success", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent( MainActivity.this, ConnectedActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"signInWithCredential:failure", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void showToast(String string) {
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG);
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

}