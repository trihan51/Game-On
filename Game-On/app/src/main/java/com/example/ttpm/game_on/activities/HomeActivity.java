package com.example.ttpm.game_on.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ttpm.game_on.fragments.PageFragment;
import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.fragments.UserProfileFragment;
import com.example.ttpm.game_on.fragments.HostSearchFragment;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (QueryPreferences.getStoredSessionId(this) == null) {
            MenuItem currentSessionMenuItem = menu.findItem(R.id.action_current_session);
            currentSessionMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logOut:
                ParseUser currentUser1 = ParseUser.getCurrentUser();
                String currentuses = currentUser1.getUsername();
                Toast.makeText(this, currentuses + " has logged out.", Toast.LENGTH_LONG).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();// this will now be null
                if (currentUser != null) {
                    Toast.makeText(this, "Error logging out!", Toast.LENGTH_LONG).show();
                } else {
                    Intent gohome = new Intent(this, SplashActivity.class);
                    startActivity(gohome);
                    this.finish();
                }
                return true;
            case R.id.action_current_session:
                Intent intent = SessionActivity.newIntent(this);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: return UserProfileFragment.newInstance();
                case 1: return HostSearchFragment.newInstance();
                default: return PageFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
