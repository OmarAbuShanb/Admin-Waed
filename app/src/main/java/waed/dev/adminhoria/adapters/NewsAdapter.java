package waed.dev.adminhoria.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.databinding.ItemNewsBinding;
import waed.dev.adminhoria.models.News;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<News> news;
    private NewsListListener newsListListener;

    public void setNewsListCallback(NewsListListener newsListListener) {
        this.newsListListener = newsListListener;
    }

    public NewsAdapter() {
        this.news = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    public void removeItem(String id) {
        for (int i = 0; i < news.size(); i++) {
            if (news.get(i).getUuid().equals(id)) {
                news.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News model = news.get(position);
        holder.bind(model);

        holder.setNewsListCallback(newsListListener);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final ItemNewsBinding binding;
        private final Context context;
        private NewsListListener newsListListener;

        public void setNewsListCallback(NewsListListener newsListListener) {
            this.newsListListener = newsListListener;
        }

        public NewsViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(News model) {
            binding.newsTitle.setText(model.getTitle());

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getImageUrl())
                    .into(binding.ivNews);

            binding.buNewsCard.setOnClickListener(v -> newsListListener.onClickItemListener(model));

            binding.btnDeleteNews.setOnClickListener(v ->
                    newsListListener.onClickDeleteListener(model.getUuid(), model.getImageUrl()));
        }
    }

    public interface NewsListListener {
        void onClickItemListener(News model);

        void onClickDeleteListener(String newsId,String imageUrl);
    }
}
