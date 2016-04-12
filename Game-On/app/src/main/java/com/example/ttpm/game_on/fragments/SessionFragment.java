package com.example.ttpm.game_on.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.HomePagerActivity;
import com.example.ttpm.game_on.activities.SessionActivity;
import com.example.ttpm.game_on.models.GameOnSession;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tri Han on 2/10/2016.
 */
public class SessionFragment extends VisibleFragment {

    private static final String ARG_CURRENT_LOCATION = "com.example.ttpm.game_on.current_location";

    private static final String TAG = "ERROR";

    ParseUser sessionHostName = new ParseUser();
    private boolean mCurrentUserIsHost;

    private GameOnSession mCurrentGameOnSession;
    private LinearLayout mSessionInfoOutput;
    private TextView mTimerTextView;
    private Button mConfirmButton;
    private Button mLeaveButton;

    private GoogleMap mMap;
    private MapView mapView;
    private Location mCurrentLocation;
    CameraUpdate cu;

    LatLngBounds.Builder builder;

    private CountDownTimer mCountDownTimer;

    public static SessionFragment newInstance(Location currentLocation) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_CURRENT_LOCATION, currentLocation);

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
        mCurrentLocation = (Location) getArguments().getParcelable(ARG_CURRENT_LOCATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_session, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mMap = mapView.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.3353, -121.8813), 15);
        mMap.animateCamera(cameraUpdate);

        ParseQuery<GameOnSession> query = ParseQuery.getQuery(GameOnSession.class);
        query.whereEqualTo("objectId", QueryPreferences.getStoredSessionId(getActivity()));
        query.findInBackground(new FindCallback<GameOnSession>() {
            @Override
            public void done(List<GameOnSession> list, ParseException e) {
            if (e == null) {

                //Set the host of the game session to the pointer of the User of this session
                try {
                    sessionHostName = list.get(0).getParseUser("host").fetchIfNeeded();
                } catch (ParseException ex) {
                    Log.d("GAMEONSESSION", "session fetchifneeded: " + ex.toString());
                }

                mCurrentGameOnSession = list.get(0);
                Log.d("GAMEONSESSION", "onCreateView: mCurrent " + mCurrentGameOnSession.getNumberOfParticipants());
                mCurrentUserIsHost = (mCurrentGameOnSession.getHost().getObjectId() == ParseUser.getCurrentUser().getObjectId());

                mSessionInfoOutput = (LinearLayout) view.findViewById(R.id.session_participant_container);

                TextView boardGameTextView = (TextView) view.findViewById(R.id.session_game_game_name);
                boardGameTextView.setText(mCurrentGameOnSession.getGameTitle());
                TextView hostNameTextView = (TextView) view.findViewById(R.id.session_game_host_name);
                hostNameTextView.setText("Host: " + sessionHostName.getUsername());

                displaySessionParticipants(mSessionInfoOutput, mCurrentGameOnSession);

                mTimerTextView = (TextView) view.findViewById(R.id.timerTextView);
                if (!mCurrentUserIsHost) {
                    mTimerTextView.setText(R.string.time_left);
                } else {
                    startTimer();
                }

                mConfirmButton = (Button) view.findViewById(R.id.confirmButton);
                mConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCountDownTimer.cancel();
                        // user has confirmed to start session. do appropriate actions here.
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.session_confirmed_message)
                                .setTitle(R.string.session_confirmed_title)
                                .setPositiveButton(R.string.dialog_confirmed_button_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mCurrentGameOnSession.setOpenStatus(false);
                                        mCurrentGameOnSession.saveInBackground();
                                        QueryPreferences.setStoredSessionId(getActivity(), null);
                                        sendUserBackToHomePagerActivity();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                if (!mCurrentUserIsHost) {
                    mConfirmButton.setVisibility(View.GONE);
                }

                mLeaveButton = (Button) view.findViewById(R.id.leaveButton);
                mLeaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performLeaveActions();
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

        Button btn = (Button) view.findViewById(R.id.session_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("GameOnSession");
            query.whereEqualTo("objectId", QueryPreferences.getStoredSessionId(getActivity()));
            Log.d("GAMEONSESSION", "SessionID: " + QueryPreferences.getStoredSessionId(getActivity()));
//            query.clearCachedResult();
            Log.d("GAMEONSESSION", "Has cached result? " + query.hasCachedResult());
            Log.d("GAMEONSESSION", "Cleared cached result");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    ParseObject g = objects.get(0);
                    JSONArray a = g.getJSONArray("participants");
                    Log.d("GAMEONSESSION", "Session players: " + a.toString());
                    Toast.makeText(getContext(), a.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            }
        });

        return view;
    }

    /**
     * Todo: possible problems is that we have to notify adapter that dataset has changed
     * Should add a button to session page and then click it to see if it queries right
     */

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Log.d("GAMEONSESSION", "onResume");
        ParseQuery<GameOnSession> query = ParseQuery.getQuery(GameOnSession.class);
        query.whereEqualTo("objectId", QueryPreferences.getStoredSessionId(getActivity()));
        query.findInBackground(new FindCallback<GameOnSession>() {
            @Override
            public void done(List<GameOnSession> objects, ParseException e) {
                mCurrentGameOnSession = objects.get(0);
                Log.d("GAMEONSESSION", "onResume: mCurrent " + mCurrentGameOnSession.getNumberOfParticipants());
                displaySessionParticipants(mSessionInfoOutput, mCurrentGameOnSession);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void displaySessionParticipants(LinearLayout displayArea, GameOnSession session) {
        JSONArray joinPlayerObjIds = new JSONArray();
        GameOnSession currSession = session;
        try {
            joinPlayerObjIds = currSession.getParticipants();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("GAMEONSESSION", "displaySession #: " + joinPlayerObjIds.toString());
        int length = joinPlayerObjIds.length();

        for (int i = 0; i < length; i++) {
            try {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("objectId", joinPlayerObjIds.getString(i));
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        displaySessionParticipantsEmail(mSessionInfoOutput, objects.get(0));
                    }

                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(currSession != null) {
            updateUserMarker();
        }
    }

    private void startTimer() {

        long thirtyMinutes = 1000 * 60 * 30; // (1000 milliseconds/sec * 60 sec/min * 30 min)
        long minute = 1000 * 60;
        long tickLength = 1000; // 1 second

        mCountDownTimer = new CountDownTimer(thirtyMinutes, tickLength) {
            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText("" + String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                mTimerTextView.setText(R.string.timed_out_text);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.session_timed_out_message)
                        .setTitle(R.string.session_timed_out_title)
                        .setPositiveButton(R.string.dialog_timed_out_button_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                performLeaveActions();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        mCountDownTimer.start();
    }

    private void displaySessionParticipantsEmail(LinearLayout displayArea, ParseUser user)
    {
        TextView participantEmailTextView = new TextView(getActivity());
        participantEmailTextView.setText("Participant: " + user.getUsername());
        displayArea.addView(participantEmailTextView);
    }

    private void performLeaveActions() {
        removeCurrentUserFromSession();
        sendUserBackToHomePagerActivity();
    }

    private void removeCurrentUserFromSession() {
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
    }

    private void sendUserBackToHomePagerActivity() {
        Intent intent = new Intent(getActivity(), HomePagerActivity.class);
        startActivity(intent);
    }

    private void updateHostMarkers()
    {
        /*ParseGeoPoint point = mCurrentGameOnSession.getLocation();


            Marker marker = mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(point.getLatitude(), point.getLongitude()))
                            .title("Host")
            ); */
    }

    private void updateUserMarker()
    {
        Marker marker1 = mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                        .title("You")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        );

        ParseGeoPoint point = mCurrentGameOnSession.getLocation();

        Marker marker = mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(point.getLatitude(), point.getLongitude()))
                        .title("Host")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        );

        List<Marker> markersList = new ArrayList<Marker>();
        markersList.add(marker1);
        markersList.add(marker);

        builder = new LatLngBounds.Builder();
        for (Marker m : markersList) {
            builder.include(m.getPosition());
        }

        int padding = 50;
        /**create the bounds from latlngBuilder to set into map camera*/
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        /**call the map call back to know map is loaded or not*/
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                mMap.animateCamera(cu);

            }
        });
    }
}
