package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.ttpm.game_on.activities.SplashActivity;
import com.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
public class SmokeTestSuite extends ActivityInstrumentationTestCase2<SplashActivity> {
    private Solo solo;


    public SmokeTestSuite() throws ClassNotFoundException {
        super(SplashActivity.class);
    }

 //this has been replaced by fullrun




}