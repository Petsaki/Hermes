package com.petsaki.epaketo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Epaketo);
        setContentView(R.layout.activity_main);

    }

    public void sendMessage(View view){
        Intent intent = new Intent( this, ConnectedActivity.class);
        startActivity(intent);
        finish();
    }

    public void sing_up_click(View view){
        Intent intent = new Intent( this, SingUpActivity.class);
        startActivity(intent);
    }

}