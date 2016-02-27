package com.example.ttpm.game_on.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.example.ttpm.game_on.activities.SessionActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tri Han on 2/10/2016.
 */
public class SessionFragment extends VisibleFragment {

    private static final String ARG_SESSION_ID = "session_id";

    private static final String TAG = "ERROR";

    ParseUser HostOfTheGame = new ParseUser();
    ParseUser ParticipantInGame = new ParseUser();
    private List<ParseUser> particks = new ArrayList<ParseUser>();


    private GameOnSession mCurrentGameOnSession;
    private boolean mCurrentUserIsHost;

    private LinearLayout sessionInfoOutput;
    private Button leaveButton;

    public static SessionFragment newInstance(UUID sessionId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SESSION_ID, sessionId);

        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void performActionBasedOnSessionUpdated() {
        // Refreshing the session page. NOTE (2/13/2016): NOT SURE IF THIS WORKS YET!
        Fragment newFragment = new SessionFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);

        transaction.commit();
    }

    @Override
    protected void performActionBasedOnSessionCancelled() {
        // 1. display cancellation message
        // 2. Clear session id in shared preferences
        // 3. Redirect user back to HomeActivity.
        // NOTE (2/13/2016): NOT SURE IF THIS WORKS YET!

        ((SessionActivity)getActivity()).showDialog();
        // The line above is the replacement for the Toast line below. However, not sure if it works yet.
//        Toast.makeText(getActivity(), "Session has been cancelled", Toast.LENGTH_LONG).show();
        QueryPreferences.setStoredSessionId(getActivity(), null);
        Intent intent = HomePagerActivity.newIntent(getActivity());
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_session, container, false);


        ParseQuery<GameOnSession> query = GameOnSession.getQuery();
        query.whereEqualTo("objectId", QueryPreferences.getStoredSessionId(getActivity()));
        query.include("host");
        query.findInBackground(new FindCallback<GameOnSession>() {
            @Override
            public void done(List<GameOnSession> list, ParseException e) {
                if (e == null) {

                    //Set the host of the game session to the pointer of the User of this session
                    HostOfTheGame = list.get(0).getParseUser("host");


                    mCurrentGameOnSession = list.get(0);
                    mCurrentUserIsHost = (mCurrentGameOnSession.getHost().getObjectId() == ParseUser.getCurrentUser().getObjectId());

                    sessionInfoOutput = (LinearLayout) view.findViewById(R.id.sessionInfoOutputArea);


                    displayGameTitle(sessionInfoOutput);
                    displaySessionHost(sessionInfoOutput);
                    displaySessionParticipants(sessionInfoOutput);
                   // displaySessionParticipantsEmail(sessionInfoOutput);
                    System.out.print(particks.size());
                    



                    leaveButton = (Button) view.findViewById(R.id.leaveButton);
                    leaveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mCurrentUserIsHost) {
                                mCurrentGameOnSession.deleteInBackground();
                            } else {
                                try {
                                    mCurrentGameOnSession.removeParticipant(ParseUser.getCurrentUser().getObjectId());
                                    mCurrentGameOnSession.saveInBackground();
                                } catch (JSONException e) {
                                    Log.d(TAG, "JSONException occurred: " + e);
                                }
                            }
                            QueryPreferences.setStoredSessionId(getActivity(), null);
                            Intent intent = new Intent(getActivity(), HomePagerActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    // It could be either because the session no longer exists or
                    // there really was an error when trying to query Parse.
                    // for now, we can just assume that the session no longer exists.
                    // however, in the future, we will have to do some checking.

                    // another solution for now is to use call performActionBasedOnSessionCancelled here.
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void displayGameTitle(LinearLayout displayArea) {
        TextView gameTitleTextView = new TextView(getActivity());
        gameTitleTextView.append("Game Title: " + mCurrentGameOnSession.getGameTitle() + "\n");
        displayArea.addView(gameTitleTextView);
    }

    private void displaySessionHost(LinearLayout displayArea) {
       TextView hostNameTextView = new TextView(getActivity());
        hostNameTextView.append("Host: " + HostOfTheGame.getUsername());
        displayArea.addView(hostNameTextView);

    }

    private void displaySessionParticipants( LinearLayout displayArea) {
        JSONArray participants = mCurrentGameOnSession.getParticipants();

        int length = participants.length();
        System.out.println("Length of Participants Array is:" + length);
       // particks = new ArrayList<ParseUser>();

        for ( int i = 0; i < length ; i++) {
            System.out.println("Length of Participants Array is:" + length);
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            try {
                query.whereEqualTo("objectId", participants.getString(i));

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        //ParticipantInGame = objects.get

                        for (ParseUser users : objects) {

                            particks.add(users);
                        }
                        //displaySessionParticipantsEmail(particks.get(i));

                    }

                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        try {

            for (int d = 0; d < length; d++) {
                int participantNum = d + 1;
                TextView participantNameTextView = new TextView(getActivity());
                //TextView participantNameTextView = new TextView(getActivity());
                participantNameTextView.setText("Participant " + participantNum + ": " + participants.getString(d));
                displayArea.addView(participantNameTextView);

            }
        } catch (org.json.JSONException e) {
            e.getStackTrace();
        }


    }

    private void displaySessionParticipantsEmail( LinearLayout displayArea)
    {
        for ( int i= 0; i< particks.size(); i++)
        {
              int NumberOf = i+1;
                System.out.println(particks.size());
                System.out.println("Participant:" + NumberOf + particks.get(i).getObjectId());
                TextView participantEmailTextView = new TextView(getActivity());
                participantEmailTextView.setText("Participant" + NumberOf + ": " + particks.get(i).getUsername());
            displayArea.addView(participantEmailTextView);



        }

    }


    private void displaySessionParticipantsEmailTest(LinearLayout displayArea)
    {
        for ( int i = 0; i< particks.size(); i++)
        {

            int NumberOf = i + 1;
            System.out.println(particks.size());
            System.out.println("Participant:" + NumberOf + particks.get(i).getUsername());
            TextView participantEmailTextView = new TextView(getActivity());
            participantEmailTextView.setText("Participant" + NumberOf + ": " + particks.get(i).getUsername());
            displayArea.addView(participantEmailTextView);
        }

    }


}
