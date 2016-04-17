package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;

import com.example.ttpm.game_on.activities.SplashActivity;
import com.robotium.solo.Solo;

@SuppressWarnings("rawtypes")
public class RegressionTestSuite extends ActivityInstrumentationTestCase2<SplashActivity> {
    private Solo solo;


    public RegressionTestSuite() throws ClassNotFoundException {
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

    }

    public void test_UserProfile_VerifyEmail() {
        //verify login user email address present
        assertTrue(solo.searchText("sam@sjsu.edu"));
    }

    public void test_UserProfile_VerifyRadius() {
        assertTrue(solo.searchText("Search"));
        assertTrue(solo.searchText("Radius"));
        assertTrue(solo.searchText("(miles)"));
        assertTrue(solo.searchText("NA"));
        assertTrue(solo.searchText("1"));
        assertTrue(solo.searchText("2"));
        assertTrue(solo.searchText("5"));

    }

    public void test_UserProfile_Camera() {
        solo.clickOnView(solo.getView(R.id.user_profile_change_profile_picture_button));
        assertTrue(solo.searchText("Upload"));
    }


    public void test_Join_Game() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.clickOnView(solo.getView(R.id.list_item_user_games_list_button));
        assertTrue(solo.searchText("Checkers"));
    }




}