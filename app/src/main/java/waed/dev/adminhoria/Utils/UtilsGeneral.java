package waed.dev.adminhoria.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

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
