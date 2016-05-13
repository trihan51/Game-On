package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.ttpm.game_on.activities.SplashActivity;
import com.robotium.solo.Solo;

import java.util.ArrayList;

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

    //Open Session: Test to see if list games button work
    public void testOpenSession() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.clickOnScreen(500,700,1);
        solo.sleep(2000);
        solo.clickOnScreen(60,170,1);
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);
    }

    //Host Session: Check to see if it Caverna exists
    public void testHostSession() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        assertTrue(solo.searchText("Caverna: The Cave Farmers"));
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);

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
        solo.clickOnView(solo.getView(R.id.user_profile_logout_button));
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sam@sjsu.edu");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.clickOnView(solo.getView(R.id.login_login_button));
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);

    }

    //Host Session: Cancel a hosted game
    //Test fails whenever we have another host session in there because we need to update a
    // feature so we can cancel hosted session
    //once we get host one game
/*
    public void testCancelHostedSession() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        // solo.clickOnImageButton(0);
        int count = 1;
        ArrayList<Button> buttoni = solo.getCurrentViews(Button.class);
        for (Button button : buttoni) {
            if (button.getId() == (R.id.list_item_host_games_list_button)) {
                if (count == 1) {
                    solo.clickOnView(button);
                    break;
                }
                count++;

            }

        }
       solo.clickOnButton("Leave Session");

        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);
    }
*/
}