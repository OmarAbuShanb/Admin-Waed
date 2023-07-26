package waed.dev.adminhoria.screens.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import waed.dev.adminhoria.MainActivity;
import waed.dev.adminhoria.Utils.AppSharedPreferences;
import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ActivityLoginBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;

    private FirebaseController firebaseController;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        binding.btnLogin.setOnClickListener(view -> performLogin());
    }

    private boolean checkData(String email, String password) {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
    }


    private void performLogin() {
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

        if (checkData(email, password)) {
            loadingDialog.show(getSupportFragmentManager(), "Login");
            firebaseController.login(email, password, this, new FirebaseController.FirebaseAuthCallback() {
                @Override
                public void onSuccess(String userUid) {
                    AppSharedPreferences.getInstance().putCurrentUserUID(userUid);
                    navigateToHomeScreen();
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(String errorMessage) {
                    loadingDialog.dismiss();
                    UtilsGeneral.getInstance().showSnackBar(binding.getRoot(), errorMessage);
                }
            });
        }
    }

    private void navigateToHomeScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}