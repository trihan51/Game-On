package com.example.ttpm.game_on.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.fragments.UserGameFragment;

/**
 * Created by Tony on 2/18/2016.
 */
public class UserGameActivity extends AppCompatActivity{

    private static final String EXTRA_BOARD_GAME_NAME = "com.example.ttpm.game_on.board_game_name";
    private static final String EXTRA_SEARCH_RADIUS = "com.example.ttpm.game_on.session_search_radius";
    private static final String EXTRA_CURRENT_LOCATION = "com.example.ttpm.game_on.current_location";

    public static Intent newIntent(Context packageContext,
                                   String boardGameName,
                                   String searchRadius,
                                   Location currentLocation) {
        Intent intent = new Intent(packageContext, UserGameActivity.class);
        intent.putExtra(EXTRA_BOARD_GAME_NAME, boardGameName);
        intent.putExtra(EXTRA_SEARCH_RADIUS, searchRadius);
        intent.putExtra(EXTRA_CURRENT_LOCATION, currentLocation);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        String boardGameName = (String) getIntent().getSerializableExtra(EXTRA_BOARD_GAME_NAME);
        String searchRadius = (String) getIntent().getSerializableExtra(EXTRA_SEARCH_RADIUS);
        Location currentLocation = (Location) getIntent().getParcelableExtra(EXTRA_CURRENT_LOCATION);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = UserGameFragment.newInstance(boardGameName, searchRadius, currentLocation);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
