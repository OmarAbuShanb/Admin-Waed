package waed.dev.adminhoria.Screens.Statistics;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import waed.dev.adminhoria.Tools.CustomDialogManager;
import waed.dev.adminhoria.databinding.ActivityPrisonersStatisticsBinding;

public class PrisonersStatistics extends AppCompatActivity {
    ActivityPrisonersStatisticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cardNumOfMartyrsOfCaptiveMovement.setOnClickListener(v ->
                CustomDialogManager.showDialog(
                        PrisonersStatistics.this,
                        binding.titleNumOfMartyrsOfCaptiveMovement.getText().toString()
                )
        );
    }
}