package com.example.ttpm.game_on.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.GameOnSession;
import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.HomePagerActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.List;
import java.util.UUID;

/**
 * Created by Tri Han on 2/10/2016.
 */
public class SessionFragment extends Fragment {

    private static final String ARG_SESSION_ID = "session_id";

    GameOnSession currentGameOnSession;

    LinearLayout sessionInfoOutput;
    Button cancelButton;

    public static SessionFragment newInstance(UUID sessionId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SESSION_ID, sessionId);

        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_host_session_page);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_session, container, false);
//
//        ParseQuery<GameOnSession> query = GameOnSession.getQuery();
//        query.whereEqualTo("host", ParseUser.getCurrentUser());
//        query.orderByDescending("createdAt");
//        query.findInBackground(new FindCallback<GameOnSession>() {
//            @Override
//            public void done(List<GameOnSession> list, ParseException e) {
//                if (e == null) {
//                    currentGameOnSession = list.get(0);
//                    sessionInfoOutput = (LinearLayout) view.findViewById(R.id.sessionInfoOutputArea);
//
//                    displayGameTitle(sessionInfoOutput);
//                    displaySessionHost(sessionInfoOutput);
//                    displaySessionParticipants(sessionInfoOutput);
//
//                } else {
//                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        ParseQuery<GameOnSession> query = GameOnSession.getQuery();
        query.whereEqualTo("objectId", QueryPreferences.getStoredSessionId(getActivity()));
        query.findInBackground(new FindCallback<GameOnSession>() {
            @Override
            public void done(List<GameOnSession> list, ParseException e) {
                if (e == null) {
                    currentGameOnSession = list.get(0);
                    sessionInfoOutput = (LinearLayout) view.findViewById(R.id.sessionInfoOutputArea);

                    displayGameTitle(sessionInfoOutput);
                    displaySessionHost(sessionInfoOutput);
                    displaySessionParticipants(sessionInfoOutput);

                } else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGameOnSession.deleteInBackground();

                QueryPreferences.setStoredSessionId(getActivity(), null);

                Intent intent = new Intent(getActivity(), HomePagerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void displayGameTitle(LinearLayout displayArea) {
        TextView gameTitleTextView = new TextView(getActivity());
        gameTitleTextView.append("Game Title: " + currentGameOnSession.getGameTitle() + "\n");
        displayArea.addView(gameTitleTextView);
    }

    private void displaySessionHost(LinearLayout displayArea) {
        TextView hostNameTextView = new TextView(getActivity());
        hostNameTextView.append("Host: " + currentGameOnSession.getHost().getUsername());
        displayArea.addView(hostNameTextView);
    }

    private void displaySessionParticipants(LinearLayout displayArea) {
        JSONArray participants = currentGameOnSession.getParticipants();
        int length = participants.length();

        try {
            for (int i = 0; i < length; i++) {
                TextView participantNameTextView = new TextView(getActivity());
                participantNameTextView.setText("Participant " + i + ": " + participants.getString(i));
                displayArea.addView(participantNameTextView);
            }
        } catch (org.json.JSONException e) {
            e.getStackTrace();
        }

    }


}
