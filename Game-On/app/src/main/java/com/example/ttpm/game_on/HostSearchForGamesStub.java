package com.example.ttpm.game_on;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.parse.ParseACL;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostSearchForGamesStub extends android.support.v4.app.Fragment {

    protected Button chessButton;
    public HostSearchForGamesStub() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_host_search_for_games_stub, container, false);
        RelativeLayout contentView = new RelativeLayout( getActivity() );
        // add all your stuff
      

        chessButton = (Button) view.findViewById(R.id.ChessGame);
//        chessButton.setLongClickable(true);
//        chessButton.setClickable(true);
        chessButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                post();

                Intent goToSession = new Intent(getActivity(), HostSessionPage.class);
                startActivity(goToSession);
                return true;
            }
        });

        return view;
    }

    private void post() {
        GameOnSession session = new GameOnSession();
        session.setGameTitle("chess");
        session.setHost(ParseUser.getCurrentUser());

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        session.setACL(acl);

        session.saveInBackground();
    }
    public static HostSearchForGamesStub newInstance(String text)
    {
        HostSearchForGamesStub f = new HostSearchForGamesStub();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }
}





