package waed.dev.adminhoria.screens.admin;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ActivityAddAdminBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class AddAdminActivity extends AppCompatActivity {
    private ActivityAddAdminBinding binding;

    private FirebaseController firebaseController;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        binding.btnAddAdmin.setOnClickListener(view -> performRegisterNewAdmin());
    }

    private boolean checkData(String email, String password) {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
    }

    private void performRegisterNewAdmin() {
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

        if (checkData(email, password)) {
            loadingDialog.show(getSupportFragmentManager(), "RegisterNewAdmin");

            firebaseController.registerNewAdmin(email, password, new FirebaseController.FirebaseAuthCallback() {
                @Override
                public void onSuccess(String userUid) {
                    addAdmin(userUid);
                }

                @Override
                public void onFailure(String errorMessage) {
                    UtilsGeneral.getInstance().showToast(getBaseContext(), errorMessage);
                    loadingDialog.dismiss();
                }
            });
        }
    }

    private void addAdmin(String userUid) {
        firebaseController.addAdmin(userUid, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                UtilsGeneral.getInstance().showToast(getBaseContext(), "تم الاضافة بنجاح");
                loadingDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                UtilsGeneral.getInstance().showToast(getBaseContext(), errorMessage);
                loadingDialog.dismiss();
            }
        });
    }
}