package com.example.ttpm.game_on.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.fragments.HostSearchFragment;
import com.example.ttpm.game_on.fragments.UserSearchFragment;
import com.example.ttpm.game_on.fragments.UserProfileFragment;
import com.parse.ParseUser;

public class HomePagerActivity extends AppCompatActivity {

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, HomePagerActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pager);

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_home_pager_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return UserProfileFragment.newInstance();
                    case 1:
                        return UserSearchFragment.newInstance();
                    default:
                        return HostSearchFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (QueryPreferences.getStoredSessionId(this) == null) {
            MenuItem currentSessionMenuItem = menu.findItem(R.id.menu_action_current_session);
            currentSessionMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_log_out:
                ParseUser currentUser1 = ParseUser.getCurrentUser();
                String currentuses = currentUser1.getUsername();
                Toast.makeText(this, currentuses + " has logged out.", Toast.LENGTH_LONG).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();// this will now be null
                if (currentUser != null) {
                    Toast.makeText(this, "Error logging out!", Toast.LENGTH_LONG).show();
                } else {
                    Intent gohome = new Intent(this, LoginActivity.class);
                    startActivity(gohome);
                    this.finish();
                }
                return true;
            case R.id.menu_action_current_session:
                Intent intent = SessionActivity.newIntent(this);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
