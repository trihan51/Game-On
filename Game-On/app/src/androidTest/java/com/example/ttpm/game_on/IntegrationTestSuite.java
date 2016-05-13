package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

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


    //Host Session: Check to see if it opens up list of games
    public void testListGamesHosted() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        assertTrue(solo.searchText("Caverna: The Cave Farmers"));
        assertTrue(solo.searchText("Checkers"));
        assertTrue(solo.searchText("Chess"));
        assertTrue(solo.searchText("Eclipse"));
        assertTrue(solo.searchText("Monopoly"));
        assertTrue(solo.searchText("One Night Ultimate Werewolf"));
        assertTrue(solo.searchText("Pandemic Legacy: Season 1"));
        assertTrue(solo.searchText("Puerto Rico"));
        assertTrue(solo.searchText("Scrabble"));
        assertTrue(solo.searchText("Settlers of Catan"));
        assertTrue(solo.searchText("Splendor"));
        assertTrue(solo.searchText("Terra Mystica"));
        assertTrue(solo.searchText("Twilight Struggle"));

        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);

    }

    //Join Session: Overview of Join Screen
    public void testPageOverviewJoinSession() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        assertTrue(solo.searchText("See List"));
        assertTrue(solo.searchText("Quick Join"));
        assertTrue(solo.searchText("open"));
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
    }

    //LogIn: Page Overview
    public void test_LogInPageOverview(){
        solo.clickOnView(solo.getView(R.id.user_profile_logout_button));
        assertTrue(solo.searchText("Game On"));
        assertTrue(solo.searchText("Login"));
        assertTrue(solo.searchText("Register"));
        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sam@sjsu.edu");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.clickOnView(solo.getView(R.id.login_login_button));
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
    }

    //LogIn: User Successfully logsIn
    public void test_LogInSuccess(){

        solo.clickOnView(solo.getView(R.id.user_profile_logout_button));
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sam@sjsu.edu");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.clickOnView(solo.getView(R.id.login_login_button));
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);

    }


}