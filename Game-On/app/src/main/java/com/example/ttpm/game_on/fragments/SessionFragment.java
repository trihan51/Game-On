package com.example.ttpm.game_on.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.HomePagerActivity;
import com.example.ttpm.game_on.activities.SessionActivity;
import com.example.ttpm.game_on.adapters.PlayerAdapter;
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
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
    private boolean mUserIsHost;

    private GameOnSession mCurrentGameOnSession;
    private GridView mSessionInfoOutput;
    private TextView mTimerTextView;
    private Button mHostStartButton;
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

        newMapView(view, savedInstanceState);

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
                    Log.d("GAMEONSESSION", "onCreateView: mCurrent " + mCurrentGameOnSession.getAllPlayerAndHostCount());
                    mUserIsHost = (mCurrentGameOnSession.getHost().getObjectId() == ParseUser.getCurrentUser().getObjectId());

                    mSessionInfoOutput = (GridView) view.findViewById(R.id.session_participant_container);
                    mSessionInfoOutput.setAdapter(new PlayerAdapter(getContext(), mCurrentGameOnSession));

                    TextView boardGameTextView = (TextView) view.findViewById(R.id.session_game_game_name);
                    boardGameTextView.setText(mCurrentGameOnSession.getGameTitle());
                    TextView hostNameTextView = (TextView) view.findViewById(R.id.session_game_host_name);

                    Resources res = getResources();
                    String hostSessionTag = res.getString(R.string.session_host_tag)
                            + " "
                            + sessionHostName.getUsername();
                    hostNameTextView.setText(hostSessionTag);

                    mTimerTextView = (TextView) view.findViewById(R.id.timerTextView);
                    if (!mUserIsHost) {
                        mTimerTextView.setText(R.string.time_left);
                    } else {
                        startTimer();
                    }

                    mHostStartButton = (Button) view.findViewById(R.id.session_host_start_button);
                    mHostStartButton.setOnClickListener(new View.OnClickListener() {
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

                    if (!mUserIsHost) {
                        mHostStartButton.setVisibility(View.GONE);
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

        if(mCurrentGameOnSession != null) {
            updateUserMarker();
        }

        return view;
    }

    private void newMapView(View view, Bundle savedInstanceState) {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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

    private void performLeaveActions() {
        removeCurrentUserFromSession();
        sendUserBackToHomePagerActivity();
    }

    private void removeCurrentUserFromSession() {
        if (mUserIsHost) {
            mCurrentGameOnSession.deleteInBackground();
        } else {
            try {
                mCurrentGameOnSession.removePlayer(ParseUser.getCurrentUser().getObjectId());
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

    private void updateHostMarker()
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
