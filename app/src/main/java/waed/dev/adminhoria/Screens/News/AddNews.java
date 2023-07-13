package waed.dev.adminhoria.Screens.News;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import waed.dev.adminhoria.databinding.ActivityAddNewsBinding;

public class AddNews extends AppCompatActivity {
    ActivityAddNewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}