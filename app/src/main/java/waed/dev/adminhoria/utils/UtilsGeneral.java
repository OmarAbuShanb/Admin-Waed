package waed.dev.adminhoria.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.util.Size;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import waed.dev.adminhoria.R;

public class UtilsGeneral {
    private static volatile UtilsGeneral Instance;

    private UtilsGeneral() {
    }

    public static synchronized UtilsGeneral getInstance() {
        if (Instance == null) {
            Instance = new UtilsGeneral();
        }
        return Instance;
    }

    public static Bitmap getThumbnailVideo(Context context, Uri contentUri) {
        Bitmap thumbnail = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                thumbnail = context.getContentResolver().loadThumbnail(
                        contentUri,
                        new Size(300, 300),
                        null
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return thumbnail;
    }

    public String getFileName(Uri uri, Context context) {
        String result = null;

        String scheme = uri.getScheme();
        System.out.println("scheme = " + scheme);
        if (scheme != null && scheme.equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    System.out.println("DISPLAY_NAME = " + result);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        if (result == null) {
            String uriPath = uri.getPath();
            System.out.println("uriPath = " + uriPath);
            if (uriPath != null) {
                int cut = uriPath.lastIndexOf('/');
                System.out.println("cut = " + cut);
                if (cut != -1) {
                    result = uriPath.substring(cut + 1);
                    System.out.println("uriPath.substring(cut + 1 = " + result);
                }
            }
        }

        if (result == null) {
            result = UUID.randomUUID().toString();
        }

        return result;
    }

    public String getStringDateFromDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return format.format(date);
    }

    public RequestBuilder<Drawable> loadImage(Context context, @NonNull String link) {
        return Glide
                .with(context)
                .load(link)
                .placeholder(R.color.place_holder_color)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
    }

    // SnackBar
    public void showSnackBar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    // Toast
    public void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
