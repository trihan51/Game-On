package com.example.ttpm.game_on.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.ForgotPasswordActivity;
import com.example.ttpm.game_on.activities.HomePagerActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Tony on 2/17/2016.
 */
public class LoginFragment extends android.support.v4.app.Fragment {

    private MaterialEditText mEmailField;
    private MaterialEditText mPasswordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailField = (MaterialEditText) view.findViewById(R.id.login_username_edittext);
        mPasswordField = (MaterialEditText) view.findViewById(R.id.login_password_edittext);

        FancyButton loginButton = (FancyButton) view.findViewById(R.id.login_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String email = mEmailField.getText().toString().trim();
            String password = mPasswordField.getText().toString().trim();

                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(getActivity(), HomePagerActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            switch (e.getCode()) {
                                case ParseException.OBJECT_NOT_FOUND: {
                                    Toast.makeText(getActivity(),
                                            "Username or password is invalid/missing",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                default: {
                                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });

        FancyButton forgotPasswordButton = (FancyButton) view.findViewById(R.id.login_forgot_password_button);
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
