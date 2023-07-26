package waed.dev.adminhoria;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import waed.dev.adminhoria.Utils.AppSharedPreferences;
import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ActivityMainBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.screens.admin.AddAdminActivity;
import waed.dev.adminhoria.screens.auth.LoginActivity;
import waed.dev.adminhoria.screens.book.PrisonersBooksActivity;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.screens.news.NewsActivity;
import waed.dev.adminhoria.screens.notification.PushNotificationActivity;
import waed.dev.adminhoria.screens.posters.PrisonersPostersActivity;
import waed.dev.adminhoria.screens.prisoners.PrisonersCardsActivity;
import waed.dev.adminhoria.screens.statistics.PrisonersStatisticsActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private UtilsGeneral utilsGeneral;

    private FirebaseController firebaseController;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.installSplashScreen(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        utilsGeneral = UtilsGeneral.getInstance();

        loadingDialog = new LoadingDialog();

        setupListeners();
    }

    private void setupListeners() {
        binding.btnAddAdmin.setOnClickListener(view -> startNewActivity(AddAdminActivity.class));
        binding.cardNews.setOnClickListener(view -> startNewActivity(NewsActivity.class));
        binding.statisticsCard.setOnClickListener(view -> startNewActivity(PrisonersStatisticsActivity.class));
        binding.booksCard.setOnClickListener(view -> startNewActivity(PrisonersBooksActivity.class));
        binding.cardsCard.setOnClickListener(view -> startNewActivity(PrisonersCardsActivity.class));
        binding.postersCard.setOnClickListener(view -> startNewActivity(PrisonersPostersActivity.class));
        binding.floatPushNotification.setOnClickListener(view -> startNewActivity(PushNotificationActivity.class));
        binding.btnUpdateUrgentNews.setOnClickListener(v -> updateUrgentNews());
    }

    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }

    private void updateUrgentNews() {
        String newsText = binding.etArgentNews.getText().toString().trim();

        if (!TextUtils.isEmpty(newsText)) {
            binding.etArgentNews.setText("");

            loadingDialog.show(getSupportFragmentManager(), "updateUrgentNews");

            firebaseController.updateUrgentNews(newsText, new FirebaseController.FirebaseCallback() {
                @Override
                public void onSuccess() {
                    utilsGeneral.showToast(getBaseContext(), "تم التحديث");
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(String errorMessage) {
                    utilsGeneral.showToast(getBaseContext(), "حصل خطأ ما :(");
                    loadingDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkUserStatus();
    }

    private void checkUserStatus() {
        String userUID = AppSharedPreferences.getInstance().getCurrentUserUID();
        if (userUID == null) {
            startNewActivity(LoginActivity.class);
            finish();
        } else {
            checkIfNotAdminServer(userUID);
        }
    }

    private void checkIfNotAdminServer(String userUID) {
        if (!userUID.equals("NydTG9x9HmZFzY3wGRwMSrx0Sim2")) {
            binding.btnAddAdmin.setVisibility(View.GONE);
        }
    }
}
