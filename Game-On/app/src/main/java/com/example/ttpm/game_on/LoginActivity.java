package com.example.ttpm.game_on;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.text.ParseException;

public class LoginActivity extends AppCompatActivity {
    protected Button loginbutton;
    protected EditText loginEmail;
    protected EditText loginPass;
    protected Button forgotpassbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginEmail = (EditText)findViewById(R.id.loginEmailForm);
        loginPass = (EditText)findViewById(R.id.loginPassForm);

        loginbutton = (Button)findViewById(R.id.LoginButton2);
        forgotpassbutton = (Button)findViewById(R.id.forgotpassbutton);
        forgotpassbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetpass = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(resetpass);
            }
        });

       loginbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String email = loginEmail.getText().toString().trim();
               String password = loginPass.getText().toString().trim();

               ParseUser.logInInBackground(email, password, new LogInCallback() {
                   @Override
                   public void done(ParseUser parseUser, com.parse.ParseException e) {
                       if (parseUser != null)
                       {
                           Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                           Intent TakeUserNearbySession = new Intent(LoginActivity.this, NearbySessionsMain.class);
                           startActivity(TakeUserNearbySession);
                           finish();
                       }
                       else
                       {
                           Toast.makeText(LoginActivity.this, "Error logging in!", Toast.LENGTH_SHORT).show();
                       }
                   }

               });
           }
       });
    }

}
