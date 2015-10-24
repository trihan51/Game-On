package com.example.ttpm.game_on;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends android.support.v4.app.Fragment {

    TextView textView;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);
        textView = (TextView)view.findViewById(R.id.textView3);

        Bundle bundle = getArguments();
        String message = Integer.toString(bundle.getInt("count"));
        textView.setText("This is the " + message + "Swipe view Page.");
    return view;
    }


}
