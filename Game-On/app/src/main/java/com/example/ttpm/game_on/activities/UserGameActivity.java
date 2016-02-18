package com.example.ttpm.game_on.activities;

import android.content.Context;
import android.content.Intent;
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

    private static final String EXTRA_BOARD_GAME_NAME =
            "com.example.ttpm.game_on.board_game_name";

    public static Intent newIntent(Context packageContext, String boardGameName) {
        Intent intent = new Intent(packageContext, UserGameActivity.class);
        intent.putExtra(EXTRA_BOARD_GAME_NAME, boardGameName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        String boardGameName = (String) getIntent()
                .getSerializableExtra(EXTRA_BOARD_GAME_NAME);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = UserGameFragment.newInstance(boardGameName);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
