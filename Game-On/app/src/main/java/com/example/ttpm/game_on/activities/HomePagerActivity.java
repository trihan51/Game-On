package com.example.ttpm.game_on.activities;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.fragments.HostSearchFragment;
import com.example.ttpm.game_on.fragments.UserSearchFragment;
import com.example.ttpm.game_on.fragments.UserProfileFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseUser;

import java.util.ArrayList;

import io.karim.MaterialTabs;

public class HomePagerActivity extends AppCompatActivity {

    private static final String TAG = "HomePagerActivity";
    private static final int REQUEST_ERROR = 0;
    private static final int REQUEST_LOCATION = 2;

    private GoogleApiClient mClient;
    private Location mCurrentLocation;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, HomePagerActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pager);

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_home_pager_view_pager);
        viewPager.setAdapter(new SamplePageAdapter(getSupportFragmentManager(), 3));
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(viewPager);

        setCurrentLocationToDefault();

        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        HomePagerActivity.this.invalidateOptionsMenu();

                        if (ActivityCompat.checkSelfPermission(HomePagerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Check Permissions Now
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomePagerActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                                // Display UI and wait for user interaction
//                                AlertDialog.Builder builder = new AlertDialog.Builder(HomePagerActivity.this);
//                                builder.setMessage("Please enable location tracking to use full service of Game On.")
//                                        .setTitle("Permission Error")
//                                        .setPositiveButton("Ok, next time", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                setCurrentLocationToDefault();
//                                            }
//                                        });
//                                AlertDialog dialog = builder.create();
//                                dialog.show();
                            } else {
                                ActivityCompat.requestPermissions(HomePagerActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION);
                            }
                        } else {
                            // permission has been granted, continue as usual
                            getCurrentLocation();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    public void onRequestPermissionResult(int requestCode,
                                          String[] permissions,
                                          int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We can now safely use the API we requested access to
                    getCurrentLocation();
                } else {
                    // Permission was denied or request was cancelled.
                    setCurrentLocationToDefault();
                }
                return;
            }
            default:
        }
    }

    private void getCurrentLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "Got a fix: " + location);
                mCurrentLocation = location;
            }
        });
    }

    private void setCurrentLocationToDefault() {
        // Default is San Jose State University's latitude and longitude
        mCurrentLocation = new Location("");
        mCurrentLocation.setLatitude(37.335264d);
        mCurrentLocation.setLongitude(-121.883239d);
    }

    @Override
    protected void onStart() {
        super.onStart();

        HomePagerActivity.this.invalidateOptionsMenu();
        mClient.connect();

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
//            @Override
//            public Fragment getItem(int position) {
//                switch (position) {
//                    case 0:
//                        return UserProfileFragment.newInstance();
//                    case 1:
//                        return UserSearchFragment.newInstance(mCurrentLocation);
//                    default:
//                        return HostSearchFragment.newInstance(mCurrentLocation);
//                }
//            }
//
//            @Override
//            public int getCount() {
//                return 3;
//            }
//        });
    }

    @Override
    public void onStop() {
        super.onStop();

        mClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // if services are unavailable, perform an action
                            // TODO: 2/24/2016 Need to implement what happens when services are unavailable

                        }
                    });

            errorDialog.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (QueryPreferences.getStoredSessionId(this) == null) {
            MenuItem currentSessionMenuItem = menu.findItem(R.id.menu_action_current_session);
            currentSessionMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_log_out:
                ParseUser currentUser = ParseUser.getCurrentUser();
                String username = currentUser.getUsername();
                Toast.makeText(this, username + " has logged out.", Toast.LENGTH_LONG).show();
                ParseUser.logOut();

                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                this.finish();

                return true;
            case R.id.menu_action_current_session:
                intent = SessionActivity.newIntent(this, mCurrentLocation);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SamplePageAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Profile", "Join", "Host"};

        private final ArrayList<String> mTitles;

        public SamplePageAdapter(FragmentManager fm, int numberOfTabs){
            super(fm);
            mTitles = new ArrayList<>();
            for (int i = 0; i < numberOfTabs; i++) {
                mTitles.add(TITLES[i]);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return UserProfileFragment.newInstance();
                case 1:
                    return UserSearchFragment.newInstance(mCurrentLocation);
                default:
                    return HostSearchFragment.newInstance(mCurrentLocation);
            }
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }
    }
}
