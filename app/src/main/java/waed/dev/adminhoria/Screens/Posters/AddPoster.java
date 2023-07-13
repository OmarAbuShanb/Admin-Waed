package waed.dev.adminhoria.Screens.Posters;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import waed.dev.adminhoria.databinding.ActivityAddPosterBinding;

public class AddPoster extends AppCompatActivity {
    ActivityAddPosterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPosterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}