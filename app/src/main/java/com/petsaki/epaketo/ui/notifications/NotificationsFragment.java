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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.petsaki.epaketo.ForgotPasswordActivity;
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

    private Button button,button5;
    private TextView usernametext,emailtext;
    private HomeActivityViewModel homeActivityViewModel,numberViewModel;
    private TextView prosParadoshView,paradothikanView;
    int prosParadosh=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notifications, container, false);

        button=view.findViewById(R.id.button);
        button5=view.findViewById(R.id.button5);
        usernametext=(TextView)view.findViewById(R.id.text_username);
        emailtext=(TextView)view.findViewById(R.id.text_email);
        prosParadoshView=(TextView)view.findViewById(R.id.textView13);
        paradothikanView=(TextView)view.findViewById(R.id.textView14);
        homeActivityViewModel = new ViewModelProvider(getActivity()).get(HomeActivityViewModel.class);
        numberViewModel = new ViewModelProvider(getActivity()).get(HomeActivityViewModel.class);


        loginuser();
        countProsParadosh();
        countParadothikan();


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.corner_up_left);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    public void loginuser(){
        mAuth=FirebaseAuth.getInstance();

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();


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

    public void countProsParadosh() {
        Query getProsParadosh = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ProsParadwsh");

        getProsParadosh.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    prosParadosh++;
                    prosParadoshView.setText(String.valueOf(prosParadosh));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getContext(), "Can not get last key", Toast.LENGTH_SHORT).show();
            }

        });
    }


    public void countParadothikan() {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Paradothikan")) {
                    Query getParadothikan = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Paradothikan");

                    getParadothikan.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int x = Integer.valueOf(String.valueOf(snapshot.getValue()));
                            paradothikanView.setText(String.valueOf(x));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}