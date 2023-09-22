package waed.dev.adminhoria;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import java.util.Objects;

import waed.dev.adminhoria.utils.AppSharedPreferences;
import waed.dev.adminhoria.utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ActivityMainBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.screens.admin.AddAdminActivity;
import waed.dev.adminhoria.screens.album.AlbumsActivity;
import waed.dev.adminhoria.screens.auth.LoginActivity;
import waed.dev.adminhoria.screens.book.PrisonersBooksActivity;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.screens.news.NewsActivity;
import waed.dev.adminhoria.screens.notification.SendNotificationActivity;
import waed.dev.adminhoria.screens.posters.PrisonersPostersActivity;
import waed.dev.adminhoria.screens.prisoners.PrisonersCardsActivity;
import waed.dev.adminhoria.screens.statistics.PrisonersStatisticsActivity;
import waed.dev.adminhoria.screens.videos.VideosActivity;
import waed.dev.adminhoria.screens.whatsapp.WhatsAppTweetsActivity;

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
        binding.btnUpdateUrgentNews.setOnClickListener(v -> updateUrgentNews());

        binding.btnAddAdmin.setOnClickListener(view -> startNewActivity(AddAdminActivity.class));
        binding.cardNews.setOnClickListener(view -> startNewActivity(NewsActivity.class));
        binding.statisticsCard.setOnClickListener(view -> startNewActivity(PrisonersStatisticsActivity.class));
        binding.booksCard.setOnClickListener(view -> startNewActivity(PrisonersBooksActivity.class));
        binding.cardsCard.setOnClickListener(view -> startNewActivity(PrisonersCardsActivity.class));
        binding.postersCard.setOnClickListener(view -> startNewActivity(PrisonersPostersActivity.class));
        binding.floatPushNotification.setOnClickListener(view -> startNewActivity(SendNotificationActivity.class));
        binding.cardVideo.setOnClickListener(view -> startNewActivity(VideosActivity.class));
        binding.btnAlbums.setOnClickListener(view -> startNewActivity(AlbumsActivity.class));
        binding.whatsappTweets.setOnClickListener(view -> startNewActivity(WhatsAppTweetsActivity.class));
    }

    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void updateUrgentNews() {
        String urgentNewsText = Objects.requireNonNull(binding.edArgentNews.getText()).toString().trim();

        if (!TextUtils.isEmpty(urgentNewsText)) {
            binding.edArgentNews.setText("");
            clearEditTextFocusIfHas();

            loadingDialog.show(getSupportFragmentManager(), "updateUrgentNews");
            firebaseController.updateUrgentNews(urgentNewsText, new FirebaseController.FirebaseCallback() {
                @Override
                public void onSuccess() {
                    utilsGeneral.showToast(getBaseContext(), getString(R.string.urgent_news_updated));
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(String errorMessage) {
                    utilsGeneral.showToast(getBaseContext(), getString(R.string.something_went_wrong));
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

    @Override
    protected void onStop() {
        super.onStop();

        clearEditTextFocusIfHas();
    }

    private void clearEditTextFocusIfHas() {
        if (binding.edArgentNews.hasFocus()) {
            binding.edArgentNews.clearFocus();
        }
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
