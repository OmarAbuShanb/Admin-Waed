package waed.dev.adminhoria.Screens.Notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityPushNotificationBinding;

public class PushNotification extends AppCompatActivity {
    ActivityPushNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPushNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}