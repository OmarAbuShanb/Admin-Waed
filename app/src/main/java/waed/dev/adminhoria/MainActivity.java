package waed.dev.adminhoria;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import waed.dev.adminhoria.Screens.Admins.AddAdmin;
import waed.dev.adminhoria.Screens.Books.PrisonersBooks;
import waed.dev.adminhoria.Screens.News.NewsActivity;
import waed.dev.adminhoria.Screens.Notification.PushNotification;
import waed.dev.adminhoria.Screens.Posters.PrisonersPosters;
import waed.dev.adminhoria.Screens.Prisoners.PrisonersCards;
import waed.dev.adminhoria.Screens.Statistics.PrisonersStatistics;
import waed.dev.adminhoria.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.statisticsCard.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, PrisonersStatistics.class)));

        binding.booksCard.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, PrisonersBooks.class)));

        binding.cardsCard.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, PrisonersCards.class)));

        binding.postersCard.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, PrisonersPosters.class)));

        binding.cardNews.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, NewsActivity.class)));

        binding.floatPushNotification.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, PushNotification.class)));

        binding.btnAddAdmin.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, AddAdmin.class)));
    }
}
