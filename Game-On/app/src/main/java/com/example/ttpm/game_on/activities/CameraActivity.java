package com.example.ttpm.game_on.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.adapters.ImageAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Tony on 3/9/2016.
 */
public class CameraActivity extends Activity {
    private static final int ACTIVITY_START_CAMERA_APP = 1111;
    private String GALLERY_LOCATION = "Game On";

    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "";
    private File mGalleryFolder;
    private static int mColumnCount = 3;
    private static int mImageWidth;
    private static int mImageHeight;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        createImageGallery();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mImageWidth = displayMetrics.widthPixels / mColumnCount;
        mImageHeight = mImageWidth * 4 / 3;

        mRecyclerView = (RecyclerView) findViewById(R.id.camera_activity_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, mColumnCount);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter imageAdapter =
                new ImageAdapter(sortFilesToLatest(mGalleryFolder), mImageWidth, mImageHeight);
        mRecyclerView.setAdapter(imageAdapter);

        final int maxMemorySize = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemorySize / 10;
    }

    public void takePhoto(View view) {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            RecyclerView.Adapter newImageAdapter =
                    new ImageAdapter(sortFilesToLatest(mGalleryFolder), mImageWidth, mImageHeight);
            mRecyclerView.swapAdapter(newImageAdapter, false);
        }
    }

    private void createImageGallery() {
        File storageDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mGalleryFolder = new File(storageDirectory, GALLERY_LOCATION);

        if(!mGalleryFolder.exists()) {
            mGalleryFolder.mkdirs();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        File image = File.createTempFile(imageFileName, ".jpg", mGalleryFolder);
        mImageFileLocation = image.getAbsolutePath();

        return image;
    }

    private Bitmap setReducedImageSize() {
        int targetImageViewWidth = mPhotoCapturedImageView.getWidth();
        int targetImageViewHeight = mPhotoCapturedImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        // Get scale factor between original image and image view
        int scaleFactor = Math.min(
                cameraImageWidth / targetImageViewWidth,
                cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(
                bitmap,
                0, 0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                true);
        mPhotoCapturedImageView.setImageBitmap(rotatedBitmap);
    }

    private File[] sortFilesToLatest(File fileImagesDir) {
        File[] files = fileImagesDir.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return Long.valueOf(rhs.lastModified()).compareTo(lhs.lastModified());
            }
        });

        return files;
    }
}
