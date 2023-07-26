package waed.dev.adminhoria.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import java.util.Objects;

import waed.dev.adminhoria.databinding.EditDialogBinding;

public class CustomDialogManager {

    public static void showDialog(Activity activity, String oldValue, ClickSaveListener listener) {
        Dialog dialog = new Dialog(activity);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        EditDialogBinding editDialogBinding = EditDialogBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(editDialogBinding.getRoot());
        editDialogBinding.etEditDialog.setText(oldValue);
        editDialogBinding.buttonOk.setOnClickListener(v -> {
            dialog.dismiss();

            String newValue = editDialogBinding.etEditDialog.getText().toString().trim();
            listener.onClickSave(newValue);
        });
        dialog.show();
    }

    public interface ClickSaveListener {
        void onClickSave(String newValue);
    }
}
