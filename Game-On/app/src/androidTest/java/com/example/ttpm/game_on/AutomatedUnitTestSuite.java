package com.example.ttpm.game_on;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.ttpm.game_on.activities.SplashActivity;
import com.robotium.solo.Solo;
import java.util.*;

@SuppressWarnings("rawtypes")
public class AutomatedUnitTestSuite extends ActivityInstrumentationTestCase2<SplashActivity> {
    private Solo solo;


    public AutomatedUnitTestSuite() throws ClassNotFoundException {
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

    //Host Session: Host a game
    public void testHostSession() {
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.RIGHT);
        assertTrue(solo.searchText("Caverna: The Cave Farmers"));
       // solo.clickOnImageButton(0);
        int count = 1;
        ArrayList<Button> buttoni = solo.getCurrentViews(Button.class);
        for(Button button: buttoni){
            if(button.getId()== (R.id.list_item_host_games_list_button)){
                if(count==1){
                    solo.clickOnView(button);
                    break;
                }
                count++;

            }
        }

        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);

    }


   //LogOut: Verify Action Bar
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

    }


    //Register: Verify functionality of the button
    public void test_register() {
        //  solo.clickOnActionBarItem(assertTrue(solo.searchText("Settings")));
      //  solo.clickOnActionBarItem(R.id.menu_action_log_out);
        solo.clickOnView(solo.getView(R.id.user_profile_logout_button));
        solo.waitForView(R.id.splash_register_button);
        assertTrue(solo.searchText("Login"));
        assertTrue(solo.searchText("Register"));
        solo.clickOnView(solo.getView(R.id.splash_register_button));

        solo.sleep(2000);
        solo.clickOnScreen(90,170,1);
        solo.clickOnView(solo.getView(R.id.splash_login_button));
        solo.enterText((EditText) solo.getView(R.id.login_username_edittext), "sam@sjsu.edu");
        solo.enterText((EditText) solo.getView(R.id.login_password_edittext), "a");
        solo.clickOnView(solo.getView(R.id.login_login_button));
        solo.scrollViewToSide(solo.getView(R.id.activity_home_pager_view_pager), solo.LEFT);
    }

}