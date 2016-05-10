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
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testRun() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.clickOnView(solo.getView(R.id.list_item_user_games_list_button));
    }

    //Host Session: Check to see if it opens up list of games
    public void testListGamesHosted() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
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

    }

    //Join Session: Overview of Join Screen
    public void testPageOverviewJoinSession() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        assertTrue(solo.searchText("See List"));
        assertTrue(solo.searchText("Quick Join"));
        assertTrue(solo.searchText("open"));
    }

    //LogIn: Page Overview
    public void test_LogInPageOverview(){
      //  solo.clickOnActionBarItem(R.id.menu_action_log_out);
        assertTrue(solo.searchText("Game On"));
        assertTrue(solo.searchText("Login"));
        assertTrue(solo.searchText("Register"));
        // solo.clickOnView(solo.getView(R.id.menu_action_current_session));
        // assertTrue(solo.searchText("Settings"));
        //  assertTrue(solo.searchText("Log Out"));
    }

    //LogIn: User Successfully logsIn
    public void test_LogInSuccess(){
    //    solo.clickOnActionBarItem(R.id.menu_action_log_out);
        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sam@sjsu.edu");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.clickOnView(solo.getView(R.id.login_login_button));

    }


}