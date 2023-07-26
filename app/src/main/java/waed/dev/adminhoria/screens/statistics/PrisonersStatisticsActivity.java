package waed.dev.adminhoria.screens.statistics;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import waed.dev.adminhoria.Tools.CustomDialogManager;
import waed.dev.adminhoria.databinding.ActivityPrisonersStatisticsBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class PrisonersStatisticsActivity extends AppCompatActivity {
    private ActivityPrisonersStatisticsBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setupListeners();
        getStatistics();
    }

    private void getStatistics() {
        binding.scrollContent.setVisibility(View.GONE);
        binding.progressStatistics.setVisibility(View.VISIBLE);

        firebaseController.getStatistics(new FirebaseController.GetStatisticsCallback() {
            @Override
            public void onSuccess(Map<String, String> statistics) {
                putData(statistics);

                binding.scrollContent.setVisibility(View.VISIBLE);
                binding.progressStatistics.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void putData(Map<String, String> statistics) {
        binding.tvNumOfMartyrsOfCaptiveMovement.setText(statistics.get("numberOfMartyrsOfCaptiveMovement"));
        binding.tvNumOfChildCaptives.setText(statistics.get("numberOfChildCaptives"));
        binding.tvNumOfGirlPrisonerInsidePrisons.setText(statistics.get("numberOfGirlPrisonerInsidePrisons"));
        binding.tvNumOfSickPrisoner.setText(statistics.get("numberOfSickPrisoner"));
        binding.tvNumOfPrisonersWhoSpentMoreThan30Scholars.setText(statistics.get("numberOfPrisonersWhoSpentMoreThan30Scholars"));
        binding.tvNumOfPrisonerSentencedToLife.setText(statistics.get("numberOfPrisonerSentencedToLife"));
        binding.tvNumOfPrisonerInsidePrisons.setText(statistics.get("numberOfPrisonerInsidePrisons"));
        binding.tvNumOfAdministrativeDetention.setText(statistics.get("numberOfAdministrativeDetention"));
    }

    private void setupListeners() {
        binding.tvNumOfMartyrsOfCaptiveMovement.setOnClickListener(v ->
                onClickStatisticNumber(v, "numberOfMartyrsOfCaptiveMovement"));
        binding.tvNumOfChildCaptives.setOnClickListener(v ->
                onClickStatisticNumber(v, "numberOfChildCaptives"));
        binding.tvNumOfGirlPrisonerInsidePrisons.setOnClickListener(v ->
                onClickStatisticNumber(v, "numberOfGirlPrisonerInsidePrisons"));
        binding.tvNumOfSickPrisoner.setOnClickListener(v ->
                onClickStatisticNumber(v, "numberOfSickPrisoner"));
        binding.tvNumOfPrisonersWhoSpentMoreThan30Scholars.setOnClickListener(v ->
                onClickStatisticNumber(v, "numberOfPrisonersWhoSpentMoreThan30Scholars"));
        binding.tvNumOfPrisonerSentencedToLife.setOnClickListener(v ->
                onClickStatisticNumber(v, "numberOfPrisonerSentencedToLife"));
        binding.tvNumOfPrisonerInsidePrisons.setOnClickListener(v ->
                onClickStatisticNumber(v, "numberOfPrisonerInsidePrisons"));
        binding.tvNumOfAdministrativeDetention.setOnClickListener(v ->
                onClickStatisticNumber(v, "numberOfAdministrativeDetention"));
    }

    private void onClickStatisticNumber(View view, String key) {
        String oldValue = ((TextView) view).getText().toString();
        CustomDialogManager.showDialog(
                PrisonersStatisticsActivity.this,
                oldValue,
                newValue -> {
                    updateStatistics(key, newValue, view);
                });
    }

    private void updateStatistics(String key, String newValue, View view) {
        loadingDialog.show(getSupportFragmentManager(), "updateStatistics");
        firebaseController.updateStatistics(key, newValue, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                ((TextView) view).setText(newValue);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }
}