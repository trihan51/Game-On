package com.example.ttpm.game_on.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPasswordActivity extends AppCompatActivity {
    protected Button resetbutton;
    protected EditText emailaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetbutton = (Button)findViewById(R.id.resetbutton);
        emailaddress = (EditText)findViewById(R.id.emailtoreset);

        resetbutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String emailaddressstring = emailaddress.getText().toString().trim();
                ParseUser.requestPasswordResetInBackground(emailaddressstring, new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if ( e == null)
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset, check your email!", Toast.LENGTH_SHORT).show();
                            Intent gohome = new Intent(ForgotPasswordActivity.this, SplashActivity.class);
                            startActivity(gohome);
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
