package waed.dev.adminhoria.screens.posters;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import waed.dev.adminhoria.databinding.ActivityAddPosterBinding;

public class AddPosterActivity extends AppCompatActivity {
    private ActivityAddPosterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPosterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}