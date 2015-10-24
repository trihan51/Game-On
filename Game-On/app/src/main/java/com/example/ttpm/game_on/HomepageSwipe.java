package com.example.ttpm.game_on;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomepageSwipe extends FragmentActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_swipe);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
    }
}
