package waed.dev.adminhoria;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import waed.dev.adminhoria.screens.admin.AddAdminActivity;
import waed.dev.adminhoria.screens.book.PrisonersBooksActivity;
import waed.dev.adminhoria.screens.news.NewsActivity;
import waed.dev.adminhoria.screens.notification.PushNotificationActivity;
import waed.dev.adminhoria.screens.posters.PrisonersPostersActivity;
import waed.dev.adminhoria.screens.prisoners.PrisonersCardsActivity;
import waed.dev.adminhoria.screens.statistics.PrisonersStatisticsActivity;
import waed.dev.adminhoria.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListeners();
    }

    private void setupListeners() {
        binding.statisticsCard.setOnClickListener(view -> startNewActivity(PrisonersStatisticsActivity.class));
        binding.booksCard.setOnClickListener(view -> startNewActivity(PrisonersBooksActivity.class));
        binding.cardsCard.setOnClickListener(view -> startNewActivity(PrisonersCardsActivity.class));
        binding.postersCard.setOnClickListener(view -> startNewActivity(PrisonersPostersActivity.class));
        binding.cardNews.setOnClickListener(view -> startNewActivity(NewsActivity.class));
        binding.floatPushNotification.setOnClickListener(view -> startNewActivity(PushNotificationActivity.class));
        binding.btnAddAdmin.setOnClickListener(view -> startNewActivity(AddAdminActivity.class));
    }

    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
    }
}
