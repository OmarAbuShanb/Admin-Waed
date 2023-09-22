package waed.dev.adminhoria.screens.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.DialogGetTextBinding;

public class GetTextDialog extends DialogFragment {
    private DialogGetTextBinding binding;

    private final @StringRes int title;
    private final @StringRes int textFieldHint;
    private final @StringRes int buttonText;
    private final ClickDialogButtonListener clickDialogButtonListener;

    public GetTextDialog(
            @StringRes int title,
            @StringRes int textFieldHint,
            @StringRes int buttonText,
            ClickDialogButtonListener clickDialogButtonListener
    ) {
        this.title = title;
        this.textFieldHint = textFieldHint;
        this.buttonText = buttonText;
        this.clickDialogButtonListener = clickDialogButtonListener;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setWindowAnimations(R.style.DialogTranslateScaleAnimation);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = DialogGetTextBinding.inflate(inflater, container, false);

        binding.tvDialogTitle.setText(title);
        binding.edDialog.setHint(textFieldHint);
        binding.btnDialog.setText(buttonText);

        binding.btnDialog.setOnClickListener(v -> {
            String text = Objects.requireNonNull(binding.edDialog.getText()).toString().trim();
            if (!TextUtils.isEmpty(text)) {
                dismiss();
                clickDialogButtonListener.onClickDialogButton(text);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();

        // clear edit text when dialog stop
        binding.edDialog.setText("");
    }

    public interface ClickDialogButtonListener {
        void onClickDialogButton(String text);
    }
}
