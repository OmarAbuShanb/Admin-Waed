package waed.dev.adminhoria.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.databinding.ItemAlbumBinding;
import waed.dev.adminhoria.models.Album;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumsViewHolder> {
    private ArrayList<Album> albums;
    private AlbumsListListener albumsListListener;

    public void setAlbumsListListener(AlbumsListListener albumsListListener) {
        this.albumsListListener = albumsListListener;
    }

    public AlbumsAdapter() {
        albums = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlbumBinding binding = ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AlbumsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.bind(album);
        holder.setAlbumsListListener(albumsListListener);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void removeItem(String uuid) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getUuid().equals(uuid)) {
                albums.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    static class AlbumsViewHolder extends RecyclerView.ViewHolder {
        private final ItemAlbumBinding binding;
        private final Context context;
        private AlbumsListListener albumsListListener;

        public void setAlbumsListListener(AlbumsListListener albumsListListener) {
            this.albumsListListener = albumsListListener;
        }

        public AlbumsViewHolder(@NonNull ItemAlbumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(Album model) {
            binding.albumTitle.setText(model.getTitle());

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getImageUrl())
                    .into(binding.ivAlbum);

            binding.buAlbumsCard.setOnClickListener(v ->
                    albumsListListener.onClickAlbumItemListener(model.getUuid()));

            binding.btnDeleteAlbum.setOnClickListener(v ->
                    albumsListListener.onClickDeleteListener(model));

            binding.btnUpdateAlbum.setOnClickListener(v ->
                    albumsListListener.onClickUpdateListener(model));
        }
    }

    public interface AlbumsListListener {
        void onClickAlbumItemListener(String albumUuid);

        void onClickDeleteListener(Album album);

        void onClickUpdateListener(Album model);
    }
}
