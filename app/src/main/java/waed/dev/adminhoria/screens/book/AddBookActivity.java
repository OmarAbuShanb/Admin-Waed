package waed.dev.adminhoria.screens.book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import waed.dev.adminhoria.databinding.ActivityAddBookBinding;

public class AddBookActivity extends AppCompatActivity {
    private ActivityAddBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}