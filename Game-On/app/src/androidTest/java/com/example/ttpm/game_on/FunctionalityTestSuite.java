package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;

import com.example.ttpm.game_on.activities.SplashActivity;
import com.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
public class FunctionalityTestSuite extends ActivityInstrumentationTestCase2<SplashActivity> {
    private Solo solo;


    public FunctionalityTestSuite() throws ClassNotFoundException {
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

    //Open Session: Test to see if list games button work
    public void testOpenSession() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.clickOnView(solo.getView(R.id.list_item_user_games_list_button));
    }

    //Host Session: Check to see if it opens
    public void testHostSession() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        assertTrue(solo.searchText("Caverna: The Cave Farmers"));
        solo.clickOnImageButton(0);
    }

    //UserProfile: Verification of filter games by radius
    public void testFilterGamesByRadius() {
        solo.clickOnView(solo.getView(R.id.user_profile_radioNA));
        solo.clickOnView(solo.getView(R.id.user_profile_radio1));
        solo.clickOnView(solo.getView(R.id.user_profile_radio2));
        solo.clickOnView(solo.getView(R.id.user_profile_radio3));
        //solo.clickOnView(solo.getView(R.id));

       // solo.clickOnImageButton(0);
        // solo.clickOnView(solo.getView(R.id.list_item_host_games_button));

        // solo.clickOnButton("Host Game");
    }

    public void test_Logout() {
        solo.clickOnView(solo.getView(R.id.menu_action_current_session));
        assertTrue(solo.searchText("Settings"));
        assertTrue(solo.searchText("Log Out"));
    }

}