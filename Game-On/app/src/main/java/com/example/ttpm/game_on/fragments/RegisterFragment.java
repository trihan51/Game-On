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
import com.example.ttpm.game_on.activities.HomePagerActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Pattern;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Tony on 2/17/2016.
 */
public class RegisterFragment extends android.support.v4.app.Fragment {

    private MaterialEditText mFirstNameField;
    private MaterialEditText mLastNameField;
    private MaterialEditText mUsernameField;
    private MaterialEditText mEmailField;
    private MaterialEditText mPasswordField;
    private MaterialEditText mRepeatPasswordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mFirstNameField = (MaterialEditText) view.findViewById(R.id.register_firstname_edittext);
        mLastNameField = (MaterialEditText) view.findViewById(R.id.register_lastname_edittext);
        mUsernameField = (MaterialEditText) view.findViewById(R.id.register_username_edittext);
        mEmailField = (MaterialEditText) view.findViewById(R.id.register_email_edittext);
        mPasswordField = (MaterialEditText) view.findViewById(R.id.register_password_edittext);
        mRepeatPasswordField = (MaterialEditText) view.findViewById(R.id.register_repeat_password_edittext);

        FancyButton registerButton = (FancyButton) view.findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = mFirstNameField.getText().toString().trim();
                String lastName = mLastNameField.getText().toString().trim();
                String username = mUsernameField.getText().toString().trim();
                String email = mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();
                String repeatPassword = mRepeatPasswordField.getText().toString().trim();

                Pattern domainPattern = Pattern.compile("\\S+(@sjsu\\.edu)$");
                boolean domainValid = domainPattern.matcher(email).matches();

                boolean passwordMatches = password.equals(repeatPassword);

                login(firstName, lastName, username, email, password, passwordMatches, domainValid);
            }
        });

        return view;
    }

    public void login(String firstName, String lastName, String username,
                      String email, String password, boolean passwordMatches, boolean domainValid) {
        if (passwordMatches && domainValid) {
            ParseUser user = new ParseUser();
            user.setPassword(password);
            user.setUsername(username);
            user.setEmail(email);
            user.put("firstName", firstName);
            user.put("lastName", lastName);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Intent intent = new Intent(getActivity(), HomePagerActivity.class);
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
