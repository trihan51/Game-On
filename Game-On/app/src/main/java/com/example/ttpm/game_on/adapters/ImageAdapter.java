package com.example.ttpm.game_on.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ttpm.game_on.BitmapWorkerTask;
import com.example.ttpm.game_on.R;

import java.io.File;

/**
 * Created by Tony on 3/9/2016.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private File[] mImageFiles;
    private static int mImageWidth;
    private static int mImageHeight;

    public ImageAdapter(File[] folderFiles, int imageWidth, int imageHeight) {
        mImageFiles = folderFiles;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mImageWidth, mImageHeight);
        imageView.setLayoutParams(params);

        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File imageFile = mImageFiles[position];
        BitmapWorkerTask workerTask = new BitmapWorkerTask(holder.getImageView());
        workerTask.execute(imageFile);
    }

    @Override
    public int getItemCount() {
        return mImageFiles.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
