package com.petsaki.epaketo.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
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
import com.petsaki.epaketo.HomeActivityViewModel;
import com.petsaki.epaketo.MainActivity;
import com.petsaki.epaketo.R;
import com.petsaki.epaketo.User;

import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private String userID;
    private DatabaseReference reference;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private String loginMethod;
    private AuthUI authUI;
    private Button button;
    private TextView usernametext,emailtext;
    private NotificationsViewModel notificationsViewModel;
    private HomeActivityViewModel homeActivityViewModel,numberViewModel;
    private TextView testview;
    int number = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notifications, container, false);
        button=view.findViewById(R.id.button);
        usernametext=(TextView)view.findViewById(R.id.text_username);
        emailtext=(TextView)view.findViewById(R.id.text_email);
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        homeActivityViewModel = new ViewModelProvider(getActivity()).get(HomeActivityViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        testview=(TextView)view.findViewById(R.id.testtext);
        numberViewModel = new ViewModelProvider(getActivity()).get(HomeActivityViewModel.class);
        //testview.setText(String.valueOf(numberViewModel.getNumber()));
        loginuser();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (FirebaseAuth.getInstance() != null){
                    FirebaseAuth.getInstance().signOut();
                }else{
                    mGoogleSignInClient.signOut();
                }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

                 */

                //Einai pio swstos tropos alla den douleuei gia kapoio logo
                if (FirebaseAuth.getInstance() != null) {
                    AuthUI.getInstance().signOut(getActivity())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        logging_out();
                                    }
                                }
                            });
                }else {
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        logging_out();
                                    }
                                }
                            });
                }
            }
        });
        return view;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //mainactivityViewModel.setmNumber(5);
//    }
    //@Override
    //public void onDestroyView() {
    //    super.onDestroyView();
    //    Toast.makeText(getActivity(), "This is my Toast message!",
    //            Toast.LENGTH_LONG).show();
    //    notificationsViewModel.setmNumber(5);
    //}

    public void loginuser(){
        mAuth=FirebaseAuth.getInstance();

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
        usernametext.setText("MPHKA");


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
                        Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
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
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            }
        }
    }

    public void logging_out(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.corner_down_right,R.anim.slide_out_right);
        getActivity().finish();

    }
}