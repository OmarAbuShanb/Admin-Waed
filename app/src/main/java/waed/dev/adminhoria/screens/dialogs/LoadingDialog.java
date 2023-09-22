package waed.dev.adminhoria.screens.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.DialogLoadingBinding;

public class LoadingDialog extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setWindowAnimations(R.style.DialogAlphaScaleAnimation);
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        DialogLoadingBinding binding = DialogLoadingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
