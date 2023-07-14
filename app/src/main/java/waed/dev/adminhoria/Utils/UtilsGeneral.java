package waed.dev.adminhoria.Utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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

    // SnackBar
    public void showSnackBar(View view, String text) {
        showSnackBar(view,text,Snackbar.LENGTH_SHORT);
    }
    public void showSnackBar(View view, String text,int duration) {
        if (duration < -1 || duration > 0){
            return;
        }
        Snackbar.make(view,text,duration).show();
    }

    // Toast
    public void showToast(Context context, String text) {
        showToast(context,text,Toast.LENGTH_SHORT);
    }
    public void showToast(Context context, String text, int duration) {
        if (duration < -1 || duration > 0){
            return;
        }
        Toast.makeText(context,text,duration).show();
    }

}
