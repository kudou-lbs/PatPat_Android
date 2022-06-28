package com.lbs.patpat;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class AdaptiveBackground extends CustomTarget<Drawable> {


    private View layout;

    public AdaptiveBackground(View layout) {
        this.layout = layout;

    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

        int layoutWidth = layout.getWidth();
        int layoutHeight = layout.getHeight();
        int figWidth = resource.getIntrinsicWidth();
        int figHeight = resource.getIntrinsicHeight();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
        Bitmap bm = bitmapDrawable.getBitmap();
        float figRatio = (float) figWidth / (float) figHeight;
        float layoutRatio = (float) layoutWidth / (float) layoutHeight;
        Bitmap resBitmap;
        if (figRatio < layoutRatio) { //垂直剪裁
            Bitmap tmp = Bitmap.createScaledBitmap(bm, layoutWidth, figHeight * layoutWidth / figWidth, true);
            resBitmap = Bitmap.createBitmap(tmp, 0, (int) ((figHeight * layoutWidth / figWidth - layoutHeight) / 2), layoutWidth, layoutHeight);
            //Log.d("TEST垂直", String.valueOf((figHeight * layoutWidth / figWidth - layoutHeight) / 2));
        } else if (figRatio > layoutRatio) {   //水平剪裁
            Bitmap tmp = Bitmap.createScaledBitmap(bm, figWidth * layoutHeight / figHeight, layoutHeight, true);
            resBitmap = Bitmap.createBitmap(tmp, (int) ((figWidth * layoutHeight / figHeight - layoutWidth) / 2), 0, layoutWidth, layoutHeight);
            //Log.d("TEST水平", String.valueOf((figWidth * layoutHeight / figHeight - layoutWidth) / 2));
        } else {
            resBitmap = Bitmap.createScaledBitmap(bm, layoutWidth, layoutHeight, true);
        }

        Drawable res = new BitmapDrawable(resBitmap);
        layout.setBackground(res);
        layout.getBackground().setAlpha(230);
        //resBitmap.recycle();

    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {

    }

}
