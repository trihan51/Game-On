package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

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

        solo.clickOnScreen(60,170,1);
        solo.sleep(2000);

    }


    public void test_Join_Game() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.clickOnView(solo.getView(R.id.list_item_user_games_list_button));
        assertTrue(solo.searchText("Caverna: The Cave Farmers"));
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
    }

    //Join Session: See List to view the details of sessions currently open
    public void testSeeListDetail() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.clickOnView(solo.getView(R.id.list_item_user_games_list_button));
        assertTrue(solo.searchText("Caverna: The Cave Farmers"));
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);

    }

    //Join Session: Quick Join a game
    public void testQuickJoin() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.clickOnView(solo.getView(R.id.list_item_user_games_quick_button));
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);

    }



}