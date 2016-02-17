package com.example.ttpm.game_on.activities;

import android.support.v4.app.Fragment;

import com.example.ttpm.game_on.fragments.RegisterFragment;

public class RegisterActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }
}
