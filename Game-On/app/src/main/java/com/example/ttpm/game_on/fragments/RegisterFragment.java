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
import com.example.ttpm.game_on.activities.HomeActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

/**
 * Created by Tony on 2/17/2016.
 */
public class RegisterFragment extends android.support.v4.app.Fragment {

    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mRepeatPasswordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mUsernameField = (EditText) view.findViewById(R.id.register_email_edittext);
        mPasswordField = (EditText) view.findViewById(R.id.register_password_edittext);
        mRepeatPasswordField = (EditText) view.findViewById(R.id.register_repeat_password_edittext);

        Button registerButton = (Button) view.findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mUsernameField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();
                String repeatPassword = mRepeatPasswordField.getText().toString().trim();

                Pattern domainPattern = Pattern.compile("\\S+(@sjsu\\.edu)$");
                boolean domainValid = domainPattern.matcher(email).matches();

                boolean passwordMatches = password.equals(repeatPassword);

                login(email, password, passwordMatches, domainValid);
            }
        });

        return view;
    }

    public void login(String email, String password, boolean passwordMatches, boolean domainValid) {
        if (passwordMatches && domainValid) {
            ParseUser user = new ParseUser();
            user.setPassword(password);
            user.setUsername(email);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getActivity(), "There was an error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            if (!passwordMatches) {
                Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (!domainValid) {
                Toast.makeText(getActivity(), "Enter valid '@sjsu.edu' email address", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
