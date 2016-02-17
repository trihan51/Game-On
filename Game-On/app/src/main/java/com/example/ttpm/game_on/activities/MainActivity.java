package com.example.ttpm.game_on.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ttpm.game_on.R;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private Button loginbutton;
    protected Button registerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            Intent gomainscreen = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(gomainscreen);
        } else {
            // show the signup or login screen
        }

        loginbutton = (Button)findViewById(R.id.LoginButton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taketologinScreen = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(taketologinScreen);
                finish();
            }
        });

        registerbutton = (Button) findViewById(R.id.RegisterButton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taketoRegisterScreen = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(taketoRegisterScreen);
            }
        });
    }


}
