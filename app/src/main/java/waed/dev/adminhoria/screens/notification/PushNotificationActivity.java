package waed.dev.adminhoria.screens.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import waed.dev.adminhoria.databinding.ActivityPushNotificationBinding;

public class PushNotificationActivity extends AppCompatActivity {
    private ActivityPushNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPushNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}