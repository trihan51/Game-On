package com.example.ttpm.game_on.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.ForgotPasswordActivity;
import com.example.ttpm.game_on.activities.HomeActivity;
import com.parse.LogInCallback;
import com.parse.ParseUser;

/**
 * Created by Tony on 2/17/2016.
 */
public class LoginFragment extends android.support.v4.app.Fragment {

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailField = (EditText) view.findViewById(R.id.login_email_edittext);
        mPasswordField = (EditText) view.findViewById(R.id.login_password_edittext);

        Button loginButton = (Button) view.findViewById(R.id.login_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();

                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                        if (parseUser != null) {
                            Toast.makeText(getActivity(), "Successfully logged in!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Error logging in!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button forgotPasswordButton = (Button) view.findViewById(R.id.login_forgot_password_button);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
