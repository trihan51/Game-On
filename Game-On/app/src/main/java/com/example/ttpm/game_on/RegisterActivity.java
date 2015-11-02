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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    protected Button registerbutton;
    protected EditText registerUsername;
    protected EditText registerPassword;
    protected EditText registerPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerbutton = (Button) findViewById(R.id.RegisterButton2);
        registerUsername = (EditText)findViewById(R.id.emaillogin);
        registerPassword = (EditText)findViewById(R.id.registerpassword1);
        registerPassword2 = (EditText) findViewById(R.id.registerpassword2);

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = registerUsername.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();
                String password1 = registerPassword2.getText().toString().trim();

                // Regex for domain check
                Pattern domainPattern = Pattern.compile("\\S+(@sjsu\\.edu)$");
                // Check email has '@sjsu.edu' domain
                boolean domainValid;
                if (domainPattern.matcher(email).matches()) domainValid = true;
                else domainValid = false;

                if (password.equals(password1) && domainValid)
                {
                    ParseUser user = new ParseUser();
                    user.setPassword(password);
                    user.setUsername(email);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e)
                        {
                            if ( e == null)
                            {
                                Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_LONG).show();
                                Intent takeUserToNearbySessions = new Intent(RegisterActivity.this, NearbySessionsMain.class );
                                startActivity(takeUserToNearbySessions);

                            }else
                            {
                                Toast.makeText(RegisterActivity.this, "There was an error!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                else
                {
                    if(!password.equals(password1)) {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    } else if(!domainValid) {
                        Toast.makeText(RegisterActivity.this, "Enter valid '@sjsu.edu' email address", Toast.LENGTH_LONG).show();
                    }
                }
                //Create new user in Parse
            }
        });
    }
}
