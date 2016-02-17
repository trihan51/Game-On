package com.example.ttpm.game_on.activities;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import com.example.ttpm.game_on.fragments.ForgotPasswordFragment;

public class ForgotPasswordActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ForgotPasswordFragment();
    }
}
