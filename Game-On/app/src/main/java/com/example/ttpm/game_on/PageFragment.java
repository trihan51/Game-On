package com.example.ttpm.game_on;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends android.support.v4.app.Fragment {

    TextView textView;
    protected TextView greetinguser;
    protected Button logoutbutton;
    protected Button swipeinterfacebutton;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);
        textView = (TextView)view.findViewById(R.id.textView3);

       // Bundle bundle = getArguments();
        //String message = Integer.toString(bundle.getInt("count"));
        textView.setText(getArguments().getString("msg"));
        logoutbutton = (Button) view.findViewById(R.id.logouttbutton);

        ParseUser currentUser = ParseUser.getCurrentUser();

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser1 = ParseUser.getCurrentUser();
                String currentuses = currentUser1.getUsername();
                Toast.makeText(getActivity(), currentuses + " has logged out.", Toast.LENGTH_LONG).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();// this will now be null
                if (currentUser != null) {
                    Toast.makeText(getActivity(), "Error logging out!", Toast.LENGTH_LONG).show();
                } else {
                    Intent gohome = new Intent(getActivity(), MainActivity.class);
                    startActivity(gohome);
                    getActivity().finish();
                }

            }
        });
        return view;
    }

    public static PageFragment newInstance(String text)
    {
        PageFragment f = new PageFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }


}
