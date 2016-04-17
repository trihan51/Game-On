package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;

import com.example.ttpm.game_on.activities.SplashActivity;
import com.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
public class IntegrationTestSuite extends ActivityInstrumentationTestCase2<SplashActivity> {
    private Solo solo;


    public IntegrationTestSuite() throws ClassNotFoundException {
        super(SplashActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testRun() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.clickOnView(solo.getView(R.id.list_item_user_games_list_button));
        //      solo.clickOnView(solo.getView(R.id.list_item_join_button));
        //  solo.waitForView(solo.getView(com.example.ttpm.game_on.R.id.menu_action_log_out));
        //solo.clickOnButton("JOIN");
        //   solo.clickOnButton("Log Out");
        //    solo.clickOnView(solo.getView(com.example.ttpm.game_on.R.id.menu_action_log_out));

        /*
        try {
            solo.wait(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */


    }
}