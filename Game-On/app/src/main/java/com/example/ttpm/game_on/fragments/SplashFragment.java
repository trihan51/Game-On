package com.example.ttpm.game_on.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.HomeActivity;
import com.example.ttpm.game_on.activities.LoginActivity;
import com.example.ttpm.game_on.activities.RegisterActivity;
import com.parse.ParseUser;

/**
 * Created by Tony on 2/17/2016.
 */
public class SplashFragment extends android.support.v4.app.Fragment{

    private Button mLoginButton;
    private Button mRegisterButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent gomainscreen = new Intent(getActivity(), HomeActivity.class);
            startActivity(gomainscreen);
        }

        mLoginButton = (Button) view.findViewById(R.id.splash_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mRegisterButton = (Button) view.findViewById(R.id.splash_register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
