package waed.dev.adminhoria.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import java.util.Objects;

import waed.dev.adminhoria.databinding.EditDialogBinding;

public class CustomDialogManager {

    public static void showDialog(Activity activity, String tittle) {
        EditDialogBinding.inflate(LayoutInflater.from(activity));
        EditDialogBinding editDialogBinding;
        Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        editDialogBinding = EditDialogBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(editDialogBinding.getRoot());
        editDialogBinding.textDialogTitle.setText(tittle);
        editDialogBinding.buttonOk.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
