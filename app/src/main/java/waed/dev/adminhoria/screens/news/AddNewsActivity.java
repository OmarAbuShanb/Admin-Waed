package waed.dev.adminhoria.screens.news;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import waed.dev.adminhoria.databinding.ActivityAddNewsBinding;

public class AddNewsActivity extends AppCompatActivity {
    private ActivityAddNewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}