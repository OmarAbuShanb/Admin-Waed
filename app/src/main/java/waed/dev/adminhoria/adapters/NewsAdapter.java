package waed.dev.adminhoria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.NewsItemBinding;
import waed.dev.adminhoria.models.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<News> news;

    private NewsListListener newsListListener;
    private DeleteNewsListListener deleteNewsListListener;

    public void setNewsListCallback(NewsListListener newsListListener) {
        this.newsListListener = newsListListener;
    }

    public void setDeleteNewsCallback(DeleteNewsListListener deleteNewsListListener) {
        this.deleteNewsListListener = deleteNewsListListener;
    }

    public NewsAdapter(ArrayList<News> news) {
        this.news = news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
        notifyItemRangeInserted(0, news.size());
    }

    public void removeItem(int position) {
        news.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsItemBinding binding =
                NewsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News model = news.get(position);
        holder.bind(model, position);

        holder.setNewsListCallback(newsListListener);
        holder.setDeleteNewsCallback(deleteNewsListListener);

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final NewsItemBinding binding;
        private final Context context;
        private NewsListListener newsListListener;
        private DeleteNewsListListener deleteNewsListListener;

        protected void setNewsListCallback(NewsListListener newsListListener) {
            this.newsListListener = newsListListener;
        }

        protected void setDeleteNewsCallback(DeleteNewsListListener deleteNewsListListener) {
            this.deleteNewsListListener = deleteNewsListListener;
        }

        public NewsViewHolder(@NonNull NewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(News model, int position) {
            binding.newsTitle.setText(model.getTitle());

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getImageUrl())
                    .into(binding.ivNews);

            binding.buNewsCard.setOnClickListener(v -> newsListListener.onClickItemListener(model));

            binding.btnDeleteNews.setOnClickListener(v ->
                    deleteNewsListListener.onClickDeleteListener(model.getId(), position));
        }
    }

    public interface NewsListListener {
        void onClickItemListener(News model);
    }

    public interface DeleteNewsListListener {
        void onClickDeleteListener(String newsId, int positionItem);
    }
}
