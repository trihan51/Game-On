package com.example.ttpm.game_on.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.CameraActivity;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends android.support.v4.app.Fragment {

    private static int SELECT_FILE = 1;

    protected TextView mWelcomeMessageTextView;
    protected CircleImageView mProfileImageView;
    protected ImageButton mPictureChangeButton;

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

        mWelcomeMessageTextView = (TextView) view.findViewById(R.id.user_profile_welcome_message);
        mWelcomeMessageTextView.setText(ParseUser.getCurrentUser().getUsername());

        mProfileImageView = (CircleImageView)
                view.findViewById(R.id.user_profile_profile_image_view);

        mPictureChangeButton = (ImageButton)
                view.findViewById(R.id.user_profile_change_profile_picture_button);
        mPictureChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        updateProfilePicture();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    private void selectImage() {
        // Dialogbox items
        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };

        // Building the Dialogbox
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Upload Profile Picture");
        // Wire up the buttons inside Dialogbox
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Check for camera request
                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    startActivity(intent);
                // Check for gallery request
                } else if (items[which].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Select only images from storage
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                // Check for cancel request
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onSelectFromGalleryResult(Intent data) {
        // Access Android local db to retrieve images
        Uri selectedImageUri = data.getData();
        // Number of columns to present all data retrieved from uri
        String[] projection = { MediaStore.MediaColumns.DATA };
        CursorLoader cursorLoader = new CursorLoader(
                this.getActivity(),
                selectedImageUri,
                projection,
                null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        // Image resource location chosen by user
        String selectedImagePath = cursor.getString(column_index);

        // Manipulates image
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while(options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                options.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        // Compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        uploadToParse(bytes);
    }

    private void uploadToParse(ByteArrayOutputStream stream) {
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile(System.currentTimeMillis() + ".jpg", image);
        // Upload the image to Parse
        file.saveInBackground();

        // Retrieve current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Finds profilePicture column in User table and inserts the image
        currentUser.put("profilePicture", file);
        // Save the user
        currentUser.saveInBackground();

        updateProfilePicture();

        Toast.makeText(this.getActivity(), "Image uploaded!",
                Toast.LENGTH_SHORT).show();
    }

    private void updateProfilePicture() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if(e == null) {
                    ParseFile picture = object.getParseFile("profilePicture");
                    if(picture == null) {
                        Log.d("GAMEONSESSION", "Picture is null");
                        return;
                    }

                    picture.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if(e == null) {
                                if(data.length == 0) {
                                    Log.d("GAMEONSESSION", "Data found, but nothing to extract");
                                    return;
                                }
                                Log.d("GAMEONSESSION", "File found! Can set to imageview");

                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                mProfileImageView.setImageBitmap(bitmap);
                            } else {
                                Log.d("GAMEONSESSION", "Parsefile contains no data");
                            }
                        }
                    });
                } else {
                    Log.d("GAMEONSESSION", "No user found");
                }
            }
        });
    }
}

