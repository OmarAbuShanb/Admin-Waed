package waed.dev.adminhoria.screens.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import waed.dev.adminhoria.adapters.StatisticsAdapter;
import waed.dev.adminhoria.databinding.ActivityPrisonersStatisticsBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Statistic;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class PrisonersStatisticsActivity extends AppCompatActivity
        implements StatisticsAdapter.StatisticListListener {
    private ActivityPrisonersStatisticsBinding binding;
    private StatisticsAdapter statisticsAdapter;

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
        setupStatisticsAdapter();
        attachItemTouchHelperToRecycler();
        getStatistics();
    }

    private void setupListeners() {
        binding.floatAddStatistics.setOnClickListener(view ->
                startActivity(new Intent(getBaseContext(), AddPrisonersStatisticActivity.class)));
    }

    private void setupStatisticsAdapter() {
        statisticsAdapter = new StatisticsAdapter();
        binding.statisticsRecycler.setAdapter(statisticsAdapter);
        binding.statisticsRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.statisticsRecycler.setHasFixedSize(true);

        statisticsAdapter.setStatisticListCallback(this);
    }

    private void attachItemTouchHelperToRecycler() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String uuid = statisticsAdapter.removeStatistic(position);
                System.out.println("onSwiped " + uuid);
                deleteStatistics(uuid);
            }
        }).attachToRecyclerView(binding.statisticsRecycler);
    }

    private void getStatistics() {
        binding.progressStatistics.setVisibility(View.VISIBLE);
        binding.tvDateOfLastUpdate.setVisibility(View.GONE);

        firebaseController.getStatistics(new FirebaseController.GetDataCallback<>() {
            @Override
            public void onSuccess(ArrayList<Statistic> statistics) {
                binding.progressStatistics.setVisibility(View.GONE);

                if (!statistics.isEmpty()) {
                    statisticsAdapter.setData(statistics);
                    setDateOfLastStatisticsUpdate(statistics);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void setDateOfLastStatisticsUpdate(ArrayList<Statistic> statistics) {
        // sort statistics using timestamp
        Collections.sort(statistics, (statistic1, statistics2) ->
                statistic1.getTimestamp().compareTo(statistics2.getTimestamp()));

        Statistic lastStatistic = statistics.get(statistics.size() - 1);
        Date lastStatisticDate = lastStatistic.getTimestamp().toDate();
        String lastUpdate = UtilsGeneral.getInstance().getStringDateFromDate(lastStatisticDate);
        binding.tvDateOfLastUpdate.setText(lastUpdate);
        binding.tvDateOfLastUpdate.setVisibility(View.VISIBLE);
    }

    private void deleteStatistics(String uuid) {
        loadingDialog.show(getSupportFragmentManager(), "deleteStatistics");
        firebaseController.deleteStatistic(uuid, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deleteStatisticFiles(uuid);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteStatisticFiles(String uuid) {
        firebaseController.deleteStatisticFiles(uuid, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClickStatisticItemListener(Statistic model) {
        Intent intent = new Intent(this, AddPrisonersStatisticActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }
}