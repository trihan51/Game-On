package com.example.ttpm.game_on.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttpm.game_on.R;

/**
 * Created by Tri Han on 2/11/2016.
 */
public class DisabledFragment extends Fragment {

    public DisabledFragment() {}

    public static DisabledFragment newInstance(String text) {
        DisabledFragment f = new DisabledFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disabled, container, false);

        return view;
    }

}
