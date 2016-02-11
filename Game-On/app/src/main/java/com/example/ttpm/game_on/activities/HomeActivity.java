package com.example.ttpm.game_on.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ttpm.game_on.PageFragment;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.fragments.UserProfileFragment;
import com.example.ttpm.game_on.fragments.HostSearchFragment;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_swipe);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        viewPager = (ViewPager)findViewById(R.id.view_pager);
        //SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logOut) {
            ParseUser currentUser1 = ParseUser.getCurrentUser();
            String currentuses = currentUser1.getUsername();
            Toast.makeText(this, currentuses + " has logged out.", Toast.LENGTH_LONG).show();
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();// this will now be null
            if (currentUser != null) {
                Toast.makeText(this, "Error logging out!", Toast.LENGTH_LONG).show();
            } else {
                Intent gohome = new Intent(this, MainActivity.class);
                startActivity(gohome);
                this.finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return UserProfileFragment.newInstance("");
                //case 2: return PageFragment.newInstance("FirstFragment, Instance 1");
                //case 1: return PageFragment2.newInstance("SecondFragment, Instance 1");
                //case 1: return PageFragment3.newInstance("Third Fragment, Instance 1");
                case 1: return HostSearchFragment.newInstance("Third Fragment, Instance 1");

                default: return PageFragment.newInstance("");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
