package waed.dev.adminhoria.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.databinding.ItemVideoBinding;
import waed.dev.adminhoria.models.Video;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.NewsViewHolder> {
    private ArrayList<Video> videos;
    private VideosListListener videosListListener;

    public void setVideosListCallback(VideosListListener videosListListener) {
        this.videosListListener = videosListListener;
    }

    public VideosAdapter() {
        this.videos = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public void removeItem(String uuid) {
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).getUuid().equals(uuid)) {
                videos.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoBinding binding = ItemVideoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.bind(video);

        holder.setVideosListCallback(videosListListener);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final ItemVideoBinding binding;
        private final Context context;
        private VideosListListener videosListListener;

        public void setVideosListCallback(VideosListListener videosListListener) {
            this.videosListListener = videosListListener;
        }

        public NewsViewHolder(@NonNull ItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(Video model) {
            binding.videoTitle.setText(model.getTitle());

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getVideoImageUrl())
                    .into(binding.ivNews);

            binding.buVideoCard.setOnClickListener(v ->
                    videosListListener.onClickVideoItemListener(model));

            binding.buDeleteVideo.setOnClickListener(v ->
                    videosListListener.onClickDeleteListener(model));
        }
    }

    public interface VideosListListener {
        void onClickVideoItemListener(Video model);

        void onClickDeleteListener(Video video);
    }
}
