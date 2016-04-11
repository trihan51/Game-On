package com.example.ttpm.game_on.activities;

import android.support.v4.app.Fragment;
import com.example.ttpm.game_on.fragments.SplashFragment;

public class SplashActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SplashFragment();
    }
}
