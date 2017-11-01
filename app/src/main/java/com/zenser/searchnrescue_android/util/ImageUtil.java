package com.zenser.searchnrescue_android.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;

import com.zenser.searchnrescue_android.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class ImageUtil {
    private static final int BITMAP_TYPE_THUMBNAIL = 0;
    private static final int BITMAP_TYPE_SCALED = 1;
    private static final int BITMAP_TYPE = 2;


    public static Bitmap getBitmapFromUri(Context context, Uri photoUri) {
        return getBitmap(context, photoUri.toString(), BITMAP_TYPE);
    }

    public static Bitmap getScaledBitmapFromUri(Context context, Uri photoUri) {
        return getBitmap(context, photoUri.toString(), BITMAP_TYPE_SCALED);
    }

    public static Bitmap getThumbnailFromUri(Context context, Uri photoUri) {
        return getBitmap(context, photoUri.toString(), BITMAP_TYPE_THUMBNAIL);
    }

    private static Bitmap getBitmap(Context context, String photoUri, int bitmapType) {
        Bitmap defaultPhoto = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_default_picture_24dp);

        if (photoUri == null || photoUri.isEmpty()) {
//            Log.d(LOG_TAG, "Using default photo due empty or null uri");
            return defaultPhoto;
        }

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeBitmap(context, photoUri, options);
        options.inSampleSize = calculateInSampleSize(options, 512, 512);
        options.inJustDecodeBounds = false;
        bm = decodeBitmap(context, photoUri, options);

        if (bm == null) {
            return defaultPhoto;
        }

        switch (bitmapType) {
            case BITMAP_TYPE_THUMBNAIL:
                int thmSize = (int) (bm.getHeight() * (64.0 / bm.getWidth()));
                bm = ThumbnailUtils.extractThumbnail(bm, 64, thmSize);
                break;
        }

        return bm;

    }

    private static Bitmap decodeBitmap(Context context, String photoUri, BitmapFactory.Options options) {
        if (photoUri.contains("content://")) {
            return getBitmapByContentResolver(context, photoUri, options);
        } else {
            return getBitmapByStream(photoUri, options);
        }
    }

    private static Bitmap getBitmapByStream(String photoUri, BitmapFactory.Options options) {
        try (InputStream is = new URL(photoUri).openStream()) {
            return BitmapFactory.decodeStream(is, null, options);
        } catch (IOException e) {
       //     Log.d(LOG_TAG, "Using default photo due IOException");
            return null;
        }
    }

    private static Bitmap getBitmapByContentResolver(Context context, String photoUri, BitmapFactory.Options options) {
        try (InputStream is = context.getContentResolver().openInputStream(Uri.parse(photoUri))) {
            return BitmapFactory.decodeStream(is, null, options);
        } catch (IOException e) {
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }



}
