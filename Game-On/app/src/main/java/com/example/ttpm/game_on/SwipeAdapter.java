package com.example.ttpm.game_on;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.ttpm.game_on.fragments.PageFragment;

/**
 * Created by manbirrandhawa on 10/23/15.
 */
public class SwipeAdapter  extends FragmentStatePagerAdapter{


    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
       /* Fragment fragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count", i + 1);
        fragment.setArguments(bundle);

        return fragment;*/
        switch(pos){
            case 0: return PageFragment.newInstance("First Fragment, Instance 1");


            default: return PageFragment.newInstance("First Fragment, Instance 1");
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
