package com.example.ttpm.game_on.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.fragments.HostSearchFragment;
import com.example.ttpm.game_on.fragments.PageFragment;
import com.example.ttpm.game_on.fragments.UserProfileFragment;

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
                        return HostSearchFragment.newInstance();
                    default:
                        return PageFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }
}
