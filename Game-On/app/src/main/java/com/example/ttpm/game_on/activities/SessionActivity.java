package com.example.ttpm.game_on.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.fragments.SessionFragment;

import java.util.UUID;

public class SessionActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SessionActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new SessionFragment();
    }

}
