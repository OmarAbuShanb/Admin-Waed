package waed.dev.adminhoria.screens.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import waed.dev.adminhoria.databinding.FragmentLogoutDialogBinding;


public class LogoutDialogFragment extends BottomSheetDialogFragment {

    private FragmentLogoutDialogBinding binding;
    private LogoutDialogListener logoutDialogListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            logoutDialogListener = (LogoutDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement LogoutDialogListener Exception: " + e);
        }
    }

    public LogoutDialogFragment() {
        // require empty one
    }

    @Override
    public void onStart() {
        super.onStart();
        var dialog = getDialog();
        if (dialog!= null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLogoutDialogBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
    }

    private void setupListeners() {
        binding.btnLogout.setOnClickListener(view -> {
            if (logoutDialogListener != null) {
                logoutDialogListener.onLogoutClicked();
                dismiss();
            }
        });
        binding.btnDismiss.setOnClickListener(view -> dismiss());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logoutDialogListener = null;
    }

    public interface LogoutDialogListener {
        void onLogoutClicked();
    }
}
