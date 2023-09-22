package waed.dev.adminhoria.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ItemAlbumImageBinding;
import waed.dev.adminhoria.databinding.ItemBaseAddAlbumImageBinding;
import waed.dev.adminhoria.models.AlbumImage;

public class AlbumImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<AlbumImage> albumImages;
    private AlbumImageListListener albumImagesListListener;
    private BaseAddAlbumImagesListener baseAddAlbumImagesListener;

    private final int BaseHolder = 0;
    private final int ImageHolder = 1;

    public void setBaseAddAlbumImagesListener(BaseAddAlbumImagesListener baseAddAlbumImagesListener) {
        this.baseAddAlbumImagesListener = baseAddAlbumImagesListener;
    }

    public void setAlbumImageListCallback(AlbumImageListListener albumImagesListListener) {
        this.albumImagesListListener = albumImagesListListener;
    }

    public AlbumImagesAdapter() {
        this.albumImages = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<AlbumImage> albumImages) {
        // Base
        this.albumImages.add(new AlbumImage(""));

        this.albumImages.addAll(albumImages);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<AlbumImage> albumImages) {
        int positionStart = this.albumImages.size();
        int itemCount = albumImages.size();

        this.albumImages.addAll(albumImages);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void removeItem(String uuid) {
        for (int i = 0; i < albumImages.size(); i++) {
            if (albumImages.get(i).getUuid().equals(uuid)) {
                albumImages.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BaseHolder;
        } else {
            return ImageHolder;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == BaseHolder) {
            ItemBaseAddAlbumImageBinding binding =
                    ItemBaseAddAlbumImageBinding.inflate(inflater, parent, false);
            return new BaseAddAlbumImagesViewHolder(binding);
        } else {
            ItemAlbumImageBinding binding =
                    ItemAlbumImageBinding.inflate(inflater, parent, false);
            return new AlbumImageViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BaseHolder -> {
                ((BaseAddAlbumImagesViewHolder) holder).bind();

                ((BaseAddAlbumImagesViewHolder) holder).setBaseAddAlbumImagesListener(baseAddAlbumImagesListener);
            }
            case ImageHolder -> {
                AlbumImage model = albumImages.get(position);
                ((AlbumImageViewHolder) holder).bind(model);

                ((AlbumImageViewHolder) holder).setAlbumImageListCallback(albumImagesListListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return albumImages.size();
    }

    static class AlbumImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemAlbumImageBinding binding;
        private final Context context;
        private AlbumImageListListener albumImagesListListener;

        public void setAlbumImageListCallback(AlbumImageListListener albumImagesListListener) {
            this.albumImagesListListener = albumImagesListListener;
        }

        public AlbumImageViewHolder(@NonNull ItemAlbumImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(AlbumImage model) {
            // show new image using uri
            if (model.getImageUri() != null) {
                binding.ivAlbumImage.setImageURI(model.getImageUri());
            } else {
                // show old image using url
                UtilsGeneral.getInstance()
                        .loadImage(context, model.getImageUrl())
                        .into(binding.ivAlbumImage);
            }

            binding.btnDeleteAlbumImage.setOnClickListener(v ->
                    albumImagesListListener.onClickDeleteListener(model));
        }
    }

    static class BaseAddAlbumImagesViewHolder extends RecyclerView.ViewHolder {
        private final ItemBaseAddAlbumImageBinding binding;
        private BaseAddAlbumImagesListener listener;

        public void setBaseAddAlbumImagesListener(BaseAddAlbumImagesListener listener) {
            this.listener = listener;
        }

        public BaseAddAlbumImagesViewHolder(@NonNull ItemBaseAddAlbumImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind() {
            binding.buBaseAddAlbumImageCard.setOnClickListener(v -> listener.onClickAddListener());
        }
    }

    public interface AlbumImageListListener {
        void onClickDeleteListener(AlbumImage model);
    }

    public interface BaseAddAlbumImagesListener {
        void onClickAddListener();
    }
}
