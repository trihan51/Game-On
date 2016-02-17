package com.example.ttpm.game_on.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.SplashActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by Tony on 2/17/2016.
 */
public class ForgotPasswordFragment extends android.support.v4.app.Fragment {

    private EditText mEmailField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        Button resetButton = (Button) view.findViewById(R.id.forgot_password_reset_password_button);
        mEmailField = (EditText) view.findViewById(R.id.forgot_password_email_edittext);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = mEmailField.getText().toString().trim();
                ParseUser.requestPasswordResetInBackground(emailAddress, new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "Password reset, check your email!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), SplashActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Email not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}
