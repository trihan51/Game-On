package com.example.ttpm.game_on;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class HomepageSwipe extends FragmentActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_homepage_swipe);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        //SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

    }




    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return UserprofileFragment.newInstance("");
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
