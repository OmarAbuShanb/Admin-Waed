package waed.dev.adminhoria.screens.statistics;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import waed.dev.adminhoria.Tools.CustomDialogManager;
import waed.dev.adminhoria.databinding.ActivityPrisonersStatisticsBinding;

public class PrisonersStatisticsActivity extends AppCompatActivity {
    private ActivityPrisonersStatisticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    private void setupListeners() {
        binding.cardNumOfMartyrsOfCaptiveMovement.setOnClickListener(v ->
         CustomDialogManager
                 .showDialog(PrisonersStatisticsActivity.this,
                         binding.titleNumOfMartyrsOfCaptiveMovement
                                 .getText().toString())
        );

    }
}