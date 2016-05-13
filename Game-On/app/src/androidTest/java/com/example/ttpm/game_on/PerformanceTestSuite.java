package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.ttpm.game_on.activities.SplashActivity;
import com.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
public class PerformanceTestSuite extends ActivityInstrumentationTestCase2<SplashActivity> {
    private Solo solo;


    public PerformanceTestSuite() throws ClassNotFoundException {
        super(SplashActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
        //login
        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sam@sjsu.edu");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.clickOnView(solo.getView(R.id.login_login_button));
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);
    }

    @Override
    public void tearDown() throws Exception {
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.user_profile_logout_button));
        solo.sleep(4000);
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testRun() {



    }
}