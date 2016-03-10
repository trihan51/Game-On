package com.example.ttpm.game_on.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.adapters.ImageAdapter;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Tony on 3/9/2016.
 */
public class CameraActivity extends Activity {
    private static final int ACTIVITY_START_CAMERA_APP = 1111;
    private String GALLERY_LOCATION = "Game On";

    private String mImageFileLocation = "";
    private File mGalleryFolder;
    private static int mColumnCount = 3;
    private static int mImageWidth;
    private static int mImageHeight;
    private static LruCache<String, Bitmap> mMemoryCache;
    private static Set<SoftReference<Bitmap>> mReusableBitmaps;

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

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    mReusableBitmaps.add(new SoftReference<>(oldValue));
                }
            }

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            mReusableBitmaps = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
        }
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

        File[] mostRecentGalleryFolder = sortFilesToLatest(mGalleryFolder);

        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {
            RecyclerView.Adapter newImageAdapter =
                    new ImageAdapter(mostRecentGalleryFolder, mImageWidth, mImageHeight);
            mRecyclerView.swapAdapter(newImageAdapter, false);
        }

        uploadToParse(mostRecentGalleryFolder[0]);
    }

    private void uploadToParse(File profileImage) {
        // Create the ParseFile
        ParseFile file = new ParseFile(profileImage);
        // Upload the image to Parse
        file.saveInBackground();
        // Retrieve current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Finds profilePicture column in User table and inserts the image
        currentUser.put("profilePicture", file);
        // Save the user
        currentUser.saveInBackground();
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

    public static Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    public static void setBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private static int getBytesPerPixel(Bitmap.Config config) {
        if(config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if(config == Bitmap.Config.RGB_565) {
            return 2;
        } else if(config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if(config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    private static boolean canUseForBitmap(Bitmap candidate, BitmapFactory.Options options) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int width = options.outWidth / options.inSampleSize;
            int height = options.outHeight / options.inSampleSize;
            int byteCount = width * height * getBytesPerPixel(candidate.getConfig());

            return byteCount <= candidate.getAllocationByteCount();
        }

        return candidate.getWidth() == options.outWidth &&
                candidate.getHeight() == options.outHeight &&
                options.inSampleSize == 1;
    }

    public static Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
        Bitmap bitmap = null;
        if(mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
            synchronized (mReusableBitmaps) {
                Bitmap item;
                Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
                while(iterator.hasNext()) {
                    item = iterator.next().get();
                    if(item != null && item.isMutable()) {
                        if(canUseForBitmap(item, options)) {
                            bitmap = item;
                            iterator.remove();
                            break;
                        }
                    } else {
                        iterator.remove();
                    }
                }
            }
        }

        return bitmap;
    }
}
