package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.example.ttpm.game_on.activities.SplashActivity;
import com.robotium.solo.Solo;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class FullRun extends ActivityInstrumentationTestCase2<SplashActivity> {
    private Solo solo;


    public FullRun() throws ClassNotFoundException {
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

    public void testAll() {

        //login portion fragment_splash
        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sam@sjsu.edu");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.login_login_button));
        solo.sleep(2000);

        //initial screen once logged in fragment_user_profile
        solo.clickOnView(solo.getView((R.id.user_profile_radio1)));
        solo.sleep(2000);
        solo.clickOnView(solo.getView((R.id.user_profile_radio2)));
        solo.sleep(2000);
        solo.clickOnView(solo.getView((R.id.user_profile_radio3)));
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.user_profile_logout_button));
        solo.sleep(2000);

        //login portion fragment_splash
        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sam@sjsu.edu");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.login_login_button));
        solo.sleep(2000);

        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);

        //fragment_user_game I can't join sessions :S

        solo.clickOnView(solo.getView(R.id.menu_item_search));
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.search_src_text), "Caverna");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.search_close_btn));
        solo.sleep(2000);

        solo.clickOnView(solo.getView(R.id.menu_item_search));
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.search_src_text), "Monopoly");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.search_close_btn));
        solo.sleep(2000);

        //check join has nothing
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.sleep(2000);

        //going to logout
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.clickOnView(solo.getView(R.id.user_profile_logout_button));
        solo.sleep(2000);

        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.sleep(2000);
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sammy");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.login_login_button));
        solo.sleep(2000);

        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.sleep(2000);


        solo.clickOnView(solo.getView(R.id.list_item_host_games_game_name));
        //fragment_user_game
        solo.clickOnView(solo.getView(R.id.user_game_game_name));
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.list_item_join_button));
        solo.sleep(2000);

        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.leaveButton));
        solo.sleep(2000);

        solo.clickOnScreen(871,1210,1);

        solo.sleep(5000);

        solo.clickOnView(solo.getView(R.id.user_profile_logout_button));

    }

}