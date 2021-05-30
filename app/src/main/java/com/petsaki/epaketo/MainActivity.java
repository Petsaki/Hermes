package com.petsaki.epaketo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText emailText,passwordText;
    private GoogleSignInClient mGoogleSignInClient;
    private long lastTimeSent,spamTime = 0;
    private Toast toast;
    private Button button;
    private String old_email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //allazei to xrwma apo ta text tou status bar
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        requestPermission_location();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

        //Blepei ama einai idi sindedemenos gia na ton steilei sto home
        if (user!=null){
            Intent intent = new Intent( MainActivity.this, HomeActivity.class);
            startActivity(intent);
            this.overridePendingTransition(R.anim.slide_in_left,R.anim.corner_up_left);
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

    //Anti na exei enter to keyboard exei send kai kali thn function tou button
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

        //Elegxos lathwn
        if (email.isEmpty()) {
            emailText.setError("*Απαιτείται email!");
            emailText.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Λάθος μοτίβο email!");
            emailText.requestFocus();
            return;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("*Απαιτείται το συνθηματικό!");
            passwordText.requestFocus();
            return;
        } else {
            passwordText.setError(null);
        }
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

                    if(user.isEmailVerified()){
                        connected();
                    }else if(old_email.equals(email)){
                        if (System.currentTimeMillis() > lastTimeSent + 60000){
                            user.sendEmailVerification();
                            showToast("Ελέγξτε το email σας για να επαληθεύσετε τον λογαριασμό σας!");
                        }else{
                            showToast("Πρέπει να περιμένεις 1 λεπτό για ένα νέο email επαλήθευσης!");
                        }
                    }else if(!old_email.equals(email)){
                        user.sendEmailVerification();
                        showToast("Ελέγξτε το email σας για να επαληθεύσετε τον λογαριασμό σας!");
                        lastTimeSent = System.currentTimeMillis();
                        old_email = email;
                    }
                } else {
                    showToast("Αποτυχία σύνδεσης!");
                }
                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
            }
        });
    }

    public void sing_up_click(View view){
        Intent intent = new Intent( this, SingUpActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_left,R.anim.corner_up_left);

    }

    public void forgot_password_click(View view){
        Intent intent = new Intent( this, ForgotPasswordActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_left,R.anim.corner_up_left);

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
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this,"Σύνδεση μέσω Google: απέτυχε.", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MainActivity.this,"Σύνδεση μέσω Google: επιτυχής", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID=user.getUid();
                            Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // TODO: handle the case where the data already exists
                                    }
                                    else {
                                        String username = user.getDisplayName();
                                        String email = user.getEmail();
                                        User writeUser = new User(username, email);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(writeUser);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            connected();

                        } else {
                            Toast.makeText(MainActivity.this,"Σύνδεση μέσω Google: απέτυχε.", Toast.LENGTH_LONG).show();
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

    public void connected(){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_left,R.anim.corner_up_left);
        finish();
    }



    private void requestPermission_location() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                requestPermission_camera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                finishAffinity();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void requestPermission_camera() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                finishAffinity();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


}