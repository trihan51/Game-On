package com.example.ttpm.game_on;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.activities.HomePagerActivity;
import com.example.ttpm.game_on.activities.SplashActivity;
import com.parse.ParseUser;

public class NearbySessionsMain extends AppCompatActivity  {
    protected TextView greetinguser;
    protected Button logoutbutton;
    protected Button swipeinterfacebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_sessions_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        logoutbutton = (Button)findViewById(R.id.logoutbutton);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String currentuse = currentUser.getUsername();

        greetinguser = (TextView)findViewById(R.id.userloggedingreeting);
        greetinguser.setText("Hello, "+ currentuse + "!");

        swipeinterfacebutton = (Button)findViewById(R.id.swipehomebutton);

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser1 = ParseUser.getCurrentUser();
                String currentuses = currentUser1.getUsername();
                Toast.makeText(NearbySessionsMain.this, currentuses +" has logged out.", Toast.LENGTH_LONG).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();// this will now be null
                if (currentUser!= null)
                {
                    Toast.makeText(NearbySessionsMain.this, "Error logging out!", Toast.LENGTH_LONG).show();
                }else{
                    Intent gohome = new Intent(NearbySessionsMain.this, SplashActivity.class);
                    startActivity(gohome);
                    finish();
                }

            }
        });

        swipeinterfacebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testswipey = new Intent(NearbySessionsMain.this, HomePagerActivity.class);
                startActivity(testswipey);
                finish();

            }
        });
    }
}
