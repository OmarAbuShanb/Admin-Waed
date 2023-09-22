package waed.dev.adminhoria.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ItemStatisticsBinding;
import waed.dev.adminhoria.models.Statistic;


public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {
    private ArrayList<Statistic> statistics;
    private StatisticListListener statisticListListener;

    public StatisticsAdapter() {
        this.statistics = new ArrayList<>();
    }

    public void setStatisticListCallback(StatisticListListener statisticListListener) {
        this.statisticListListener = statisticListListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Statistic> statistics) {
        this.statistics = statistics;
        notifyDataSetChanged();
    }

    public String removeStatistic(int position) {
        String uuid = statistics.get(position).getUuid();
        statistics.remove(position);
        notifyItemRemoved(position);
        return uuid;
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStatisticsBinding binding = ItemStatisticsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new StatisticsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position) {
        Statistic model = statistics.get(position);
        holder.bind(model);

        holder.setAlbumsListCallback(statisticListListener);
    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }

    static class StatisticsViewHolder extends RecyclerView.ViewHolder {
        private final ItemStatisticsBinding binding;
        private final Context context;
        private StatisticListListener statisticListListener;

        public void setAlbumsListCallback(StatisticListListener statisticListListener) {
            this.statisticListListener = statisticListListener;
        }

        public StatisticsViewHolder(@NonNull ItemStatisticsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(Statistic model) {
            binding.tvStatisticsTitle.setText(model.getTitle());
            binding.tvStatisticsNumber.setText(String.valueOf(model.getNumber()));

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getIconUrl())
                    .into(binding.ivStatisticsImage);

            binding.statisticsCard.setOnClickListener(v ->
                    statisticListListener.onClickStatisticItemListener(model));
        }
    }

    public interface StatisticListListener {
        void onClickStatisticItemListener(Statistic model);
    }
}
