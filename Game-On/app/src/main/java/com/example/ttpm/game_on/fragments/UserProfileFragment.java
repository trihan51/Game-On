package com.example.ttpm.game_on.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.SplashActivity;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends android.support.v4.app.Fragment {

    protected TextView mWelcomeMessageTextView;
    protected Button mPictureChangeButton;

    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mWelcomeMessageTextView = (TextView)view.findViewById(R.id.user_profile_welcome_message);
        mWelcomeMessageTextView.setText("Welcome " + ParseUser.getCurrentUser().getUsername() + "!");

        mPictureChangeButton = (Button)view.findViewById(R.id.user_profile_change_profile_picture_button);

        ParseFile currentObject = ParseUser.getCurrentUser().getParseFile("profilePicture");
        ParseImageView imageView = (ParseImageView)view.findViewById(R.id.user_profile_profile_parse_image_view);
        imageView.setParseFile(currentObject);
        imageView.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
            }
        });

        return view;
    }
}

