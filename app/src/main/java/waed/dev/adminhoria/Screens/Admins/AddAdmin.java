package waed.dev.adminhoria.Screens.Admins;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddAdminBinding;

public class AddAdmin extends AppCompatActivity {
    ActivityAddAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}