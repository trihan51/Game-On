package com.example.ttpm.game_on.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ttpm.game_on.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends android.support.v4.app.Fragment {

protected TextView welcomey;
    protected Button changepicbutton;
    private int PICK_IMAGE_REQUEST = 1;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);

        welcomey = (TextView)view.findViewById(R.id.profileWelcome);
        welcomey.setText("Welcome " + ParseUser.getCurrentUser().getUsername() + "!");

        changepicbutton = (Button)view.findViewById(R.id.changepic);

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
        });*/

        ParseFile currentobject = ParseUser.getCurrentUser().getParseFile("profilePicture");
        ParseImageView imageView = (ParseImageView)view.findViewById(R.id.profileimageview);
        imageView.setParseFile(currentobject);
        imageView.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                //done?
            }
        });


        // profilepic = (ImageView) view.findViewById(R.id.profilepicimageview);
        //ParseImageView profview = (ParseImageView)findview

        //profilepic.setImage

        return view;
    }

    public static UserProfileFragment newInstance(String text) {
        UserProfileFragment f = new UserProfileFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }
}

