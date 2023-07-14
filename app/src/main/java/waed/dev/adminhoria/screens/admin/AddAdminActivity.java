package waed.dev.adminhoria.screens.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import waed.dev.adminhoria.databinding.ActivityAddAdminBinding;

public class AddAdminActivity extends AppCompatActivity {
    private ActivityAddAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}