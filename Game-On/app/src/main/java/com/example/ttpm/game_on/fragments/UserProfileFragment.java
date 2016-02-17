package com.example.ttpm.game_on.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.SplashActivity;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends android.support.v4.app.Fragment {

    protected TextView mWelcomeMessageTextView;
    protected Button mPictureChangeButton;

    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mWelcomeMessageTextView = (TextView)view.findViewById(R.id.user_profile_welcome_message);
        mWelcomeMessageTextView.setText("Welcome " + ParseUser.getCurrentUser().getUsername() + "!");

        mPictureChangeButton = (Button)view.findViewById(R.id.user_profile_change_profile_picture_button);

        /* profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        ParseUser curruser = ParseUser.getCurrentUser();
        ParseQuery query = new ParseQuery("User");
        query.getInBackground(curruser.getObjectId(), new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    Log.d("test", "The object was not found...");
                } else {
                    Log.d("test", "Retrieved the object.");
                    ParseFile fileObject = (ParseFile) parseObject.get("profilePicture");
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                        if (e == null) {
                                Log.d("test", "We've got data in data.");
                                ParseImageView imageView = (ParseImageView)getView().findViewById(R.id.profileimageview);
                                imageView.setParseFile(fileObject);
                                imageView.loadInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                    }
                                });
                        } else {
                            Log.d("test", "There was a problem downloading the data");
                        }
                    }
                });
            }
        }

        ParseObject getprofile = new ParseObject("User");
        ParseFile profpic = (ParseFile)getprofile.get("profilePicture");
        ParseUser curruser = ParseUser.getCurrentUser();
        String testy = curruser.getObjectId();

        profpic.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e==null)
                {

                }else
                {
                    ParseUser curruser = ParseUser.getCurrentUser();
                    String testy = curruser.getObjectId();
                }
            }
        });

        ParseUser curruser = ParseUser.getCurrentUser();
        String testy = curruser.getObjectId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("objectId", testy);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if ( object == null)
                {
                }else{
                   final ParseFile fileObject = (ParseFile) object.get("profilePicture");
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null)
                            {
                                ParseImageView imageView = (ParseImageView)getView().findViewById(R.id.profileimageview);
                                imageView.setParseFile(fileObject);
                                imageView.loadInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        //Loaded?
                                    }
                                });
                            } else {
                            }
                        }
                    });
                }
            }
        }); */

        ParseFile currentobject = ParseUser.getCurrentUser().getParseFile("profilePicture");
        ParseImageView imageView = (ParseImageView)view.findViewById(R.id.user_profile_profile_parse_image_view);
        imageView.setParseFile(currentobject);
        imageView.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
            }
        });

        // profilepic = (ImageView) view.findViewById(R.id.profilepicimageview);
        //ParseImageView profview = (ParseImageView)findview
        //profilepic.setImage

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_log_out:
                ParseUser currentUser1 = ParseUser.getCurrentUser();
                String currentuses = currentUser1.getUsername();
                Toast.makeText(getActivity(), currentuses + " has logged out.", Toast.LENGTH_LONG).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();// this will now be null
                if (currentUser != null) {
                    Toast.makeText(getActivity(), "Error logging out!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

