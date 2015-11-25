package com.example.ttpm.game_on;


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
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserprofileFragment extends android.support.v4.app.Fragment {

    protected TextView welcomey;
    protected Button changePicBtn;
    private int PICK_IMAGE_REQUEST = 1;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView ivImage;

    public UserprofileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);

        welcomey = (TextView) view.findViewById(R.id.profileWelcome);
        welcomey.setText("Welcome " + ParseUser.getCurrentUser().getUsername() + "!");

        changePicBtn = (Button) view.findViewById(R.id.changepic);
        changePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ivImage = (ImageView)view.findViewById(R.id.ivImage);




       /* profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/
       /* ParseUser curruser = ParseUser.getCurrentUser();
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
            }*/


        /*ParseObject getprofile = new ParseObject("User");
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
        });*/

       /* ParseUser curruser = ParseUser.getCurrentUser();
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

                            }else{

                            }
                        }
                    });

                }
            }
        });*/

        ParseFile currentobject = ParseUser.getCurrentUser().getParseFile("profilePicture");
        ParseImageView imageView = (ParseImageView) view.findViewById(R.id.profileimageview);
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


    public static UserprofileFragment newInstance(String text) {
        UserprofileFragment f = new UserprofileFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Upload Profile Picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[which].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        uploadToParse(thumbnail, bytes);

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        CursorLoader cursorLoader = new CursorLoader(this.getActivity(), selectedImageUri, projection,
                null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

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

        ivImage.setImageBitmap(bm);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        uploadToParse(bm, bytes);
    }

    private void uploadToParse(Bitmap bitmap, ByteArrayOutputStream stream) {
        byte[] image = stream.toByteArray();

        ParseFile file = new ParseFile("profile.jpeg", image);
        file.saveInBackground();

        ParseObject imgupload = new ParseObject("ImageUpload");
        imgupload.put("ImageName", "Profile Picture");
        imgupload.put("ImageFile", file);
        imgupload.saveInBackground();

        Toast.makeText(this.getActivity(), "Image uploaded!",
                Toast.LENGTH_SHORT).show();
    }
}

