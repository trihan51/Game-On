package com.example.ttpm.game_on.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ttpm.game_on.GameOnSession;
import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.HomeActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.UUID;

/**
 * Created by Tri Han on 2/10/2016.
 */
public class SessionFragment extends Fragment {

    private static final String ARG_SESSION_ID = "session_id";

    private ListView HostListView;
    private ListView ParticipantsListView;

    String sessionIdReceived;

    GameOnSession currentGameOnSession;

    LinearLayout sessionInfoOutput;
    Button cancelButton;
    TextView HostNameView;
    TextView HostEmailView;

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
        HostNameView = (TextView) view.findViewById(R.id.HostNameView);
        HostEmailView = (TextView) view.findViewById(R.id.HostEmailView);
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

        /*ParseQuery<GameOnSession> query = GameOnSession.getQuery();
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
        }); */

        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGameOnSession.deleteInBackground();

                QueryPreferences.setStoredSessionId(getActivity(), null);

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        //THIS RECEIVES THE GAMESESSION ID FROM THE PREVIOUS FRAGMENT
       Intent intent = getActivity().getIntent();
        if (intent != null)
        {
            sessionIdReceived = intent.getStringExtra("SessionIDToJoin");
            Log.d("sesssssio", sessionIdReceived);
            final ParseUser currentUSER = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("GameOnSession");
            query.whereEqualTo("objectId", sessionIdReceived);
            query.getInBackground(sessionIdReceived, new GetCallback<ParseObject>(){
                public void done(ParseObject object, ParseException e)
                {
                    if (e == null)
                    {
                        //add currentUSer to JoinedUsers
                        Log.d("currentUser", currentUSER.toString());
                        object.put("joinedUsers", currentUSER);
                        object.saveInBackground();

                    }else{

                    }
                }
            });
        }

       // ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1)

        //QUERY GAMEONSESSIONS FOR THE CLICKED GAMESESSION
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameOnSession");
        query.getInBackground(sessionIdReceived, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    //THIS PRINTS THE SESSION ID OF THE GAME CLICKED
                    Log.d("sessionID", sessionIdReceived);

                    //THIS SHOULD GET THE HOST OF THE SESSION
                   ParseUser hostuser = (ParseUser)object.getParseObject("host");


                    //THIS SHOULD PRINT THE HOST
                    Log.d("host :", hostuser.toString());

                    //NOW FIND THE HOST OF THIS SESSION AND QUERY THE USERDATABASE FOR USER & THEIR INFO
                    if (!hostuser.toString().isEmpty())
                    {
                        Log.d("host is not empty", "");
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        //query.whereEqualTo("objectId", user);
                        query.getInBackground(hostuser.toString(), new GetCallback<ParseUser>() {
                            public void done(ParseUser user, ParseException e) {
                                if (e == null) {

                                    // object will be your game score

                                    String userEmail = user.getUsername();
                                    // String Hostnamename = user.get("Name").toString();
                                    Log.d("email :", userEmail);
                                    // Log.d("name :", Hostnamename);
                                    //HostNameView.setText(Hostsemailname);
                                    //HostEmailView.setText(Hostsemailname);

                                } else {
                                    // something went wrong
                                }
                            }
                        });
                    }

                } else {
                    // something went wrong
                }
            }
        });

        /*ParseQuery<GameOnSession> query = GameOnSession.getQuery();
        query.whereEqualTo("objectId", sessionIdReceived);
        query.findInBackground(new FindCallback<GameOnSession>() {
            @Override
            public void done(List<GameOnSession> objects, ParseException e) {
                if (e == null)
                {
                    String hosty = objects.get
                }
            }
        });*/


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
//        })


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
