package com.example.ttpm.game_on;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import com.example.ttpm.game_on.activities.CameraActivity;
import com.example.ttpm.game_on.adapters.ImageAdapter;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Tony on 3/9/2016.
 */
public class SingleImageBitmapWorkerTask extends AsyncTask<File, Void, Bitmap> {

    final int TARGET_IMAGE_VIEW_HEIGHT;
    final int TARGET_IMAGE_VIEW_WIDTH;
    WeakReference<ImageView> imageViewReferences;
    private File mImageFile;

    public SingleImageBitmapWorkerTask(ImageView imageView, int imageWidth, int imageHeight) {
        imageViewReferences = new WeakReference<>(imageView);
        TARGET_IMAGE_VIEW_HEIGHT = imageHeight;
        TARGET_IMAGE_VIEW_WIDTH = imageWidth;
    }

    @Override
    protected Bitmap doInBackground(File... params) {
        mImageFile = params[0];
        Bitmap bitmap = decodeBitmapFromFile(mImageFile);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(bitmap != null && imageViewReferences != null) {
            ImageView imageView = imageViewReferences.get();
            if(imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options bmOptions) {
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;

        if(photoWidth > TARGET_IMAGE_VIEW_WIDTH || photoHeight > TARGET_IMAGE_VIEW_HEIGHT) {
            final int halfPhotoWidth = photoWidth/2;
            final int halfPhotoHeight = photoHeight/2;
            while(halfPhotoWidth/scaleFactor > TARGET_IMAGE_VIEW_WIDTH
                    || halfPhotoHeight/scaleFactor > TARGET_IMAGE_VIEW_HEIGHT) {
                scaleFactor *= 2;
            }
        }

        return scaleFactor;
    }

    private Bitmap decodeBitmapFromFile(File imageFile) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions);
        bmOptions.inJustDecodeBounds = false;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            addInBitmapOptions(bmOptions);
        }

        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }

    public File getImageFile() {
        return mImageFile;
    }

    private static void addInBitmapOptions(BitmapFactory.Options options) {
        options.inMutable = true;
        Bitmap bitmap = CameraActivity.getBitmapFromReusableSet(options);
        if(bitmap != null) {
            options.inBitmap = bitmap;
        }
    }
}
