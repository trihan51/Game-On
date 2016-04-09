package com.example.ttpm.game_on.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.CameraActivity;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends android.support.v4.app.Fragment {

    private static int SELECT_FILE = 1;

    protected TextView mUsernameTextView;
    protected CircleImageView mProfileImageView;
    protected ImageButton mPictureChangeButton;
    protected View mUploadSnackbar;
    private RadioGroup mSearchRangeRadioGroup;

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

        mUsernameTextView = (TextView) view.findViewById(R.id.user_profile_username);
        mUsernameTextView.setText(ParseUser.getCurrentUser().getUsername());

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

        mSearchRangeRadioGroup = (RadioGroup) view.findViewById(R.id.user_profile_radio_group);
        setRadiusRadioButton();
        mSearchRangeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedSearchRange = (RadioButton) group.findViewById(checkedId);
                QueryPreferences.setSearchRange(getContext(), selectedSearchRange.getText().toString());
            }
        });

        mUploadSnackbar = (View) view.findViewById(R.id.user_profile_coordinator_layout);
        if(QueryPreferences.isNewProfilePic(this.getContext())) {
            Snackbar.make(mUploadSnackbar, "Image Uploaded", Snackbar.LENGTH_SHORT).show();
            QueryPreferences.setNewProfilePic(this.getContext(), false);
        }

        updateProfilePicture();

        return view;
    }

    private void setRadiusRadioButton(){
        String selectedSearchRange = QueryPreferences.getSearchRange(getContext());
        if(selectedSearchRange.isEmpty()) {
            RadioButton btn = (RadioButton) mSearchRangeRadioGroup.getChildAt(0);
            btn.setChecked(true);
        } else {
            for (int i = 0; i < mSearchRangeRadioGroup.getChildCount(); i++) {
                RadioButton btn = (RadioButton) mSearchRangeRadioGroup.getChildAt(i);
                if (btn.getText().toString().equals(selectedSearchRange)) {
                    btn.setChecked(true);
                } else {
                    btn.setChecked(false);
                }
            }
        }
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
        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Remove Photo" };

        MaterialDialog.Builder b = new MaterialDialog.Builder(this.getActivity())
            .title("Upload Picture")
            .items(items)
            .itemsCallback(new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
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
                    } else if (items[which].equals("Remove Photo")) {
                        removeProfilePictureFromParse();
                        dialog.dismiss();
                    }
                }
            });

        MaterialDialog d = b.build();
        d.show();
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

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        // Create the ParseFile
        ParseFile file = new ParseFile(imageFileName + ".jpg", image);
        // Upload the image to Parse
        file.saveInBackground();

        // Retrieve current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Finds profilePicture column in User table and inserts the image
        currentUser.put("profilePicture", file);
        // Save the user
        currentUser.saveInBackground();

        updateProfilePicture();

        Snackbar.make(mUploadSnackbar, "Image Uploaded", Snackbar.LENGTH_SHORT).show();
    }

    private void updateProfilePicture() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
            if(e == null) {
                ParseFile picture = object.getParseFile("profilePicture");
                if(picture != null) {
                    String parseFileName = picture.getName();
                    final String pictureName = getPictureName(parseFileName);

                    picture.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                if (data.length == 0) {
                                    Log.d("GAMEON", "Data found, but nothing to extract");
                                    return;
                                }
                                Log.d("GAMEON", "File found! Can set to imageview");

                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), pictureName);
                                try {
                                    OutputStream os = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                    os.flush();
                                    os.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                Glide.with(getContext()).load(file).into(mProfileImageView);
                            } else {
                                Log.d("GAMEON", "Parsefile contains no data");
                            }
                        }
                    });
                } else {
                    Glide.with(getContext())
                            .load("")
                            .placeholder(R.drawable.com_parse_ui_facebook_login_logo)
                            .into(mProfileImageView);
                }
            } else {
                Log.d("GAMEON", "No user found");
            }
            }
        });
    }

    private String getPictureName(String input) {
        Log.d("GAMEONSESSION", input);
        String regexPattern = "(IMAGE)[\\s\\S]*";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);
        matcher.find();
        return matcher.group(0);
    }

    private void removeProfilePictureFromParse() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    object.remove("profilePicture");
                    object.saveInBackground();
                }
            }
        });

        updateProfilePicture();
        Snackbar.make(mUploadSnackbar, "Image Removed", Snackbar.LENGTH_SHORT).show();
    }
}

