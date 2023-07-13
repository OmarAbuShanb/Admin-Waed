package waed.dev.adminhoria.Screens.Books;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddBookBinding;

public class AddBook extends AppCompatActivity {
    ActivityAddBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}