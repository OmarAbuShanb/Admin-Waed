package waed.dev.adminhoria.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.databinding.ItemPosterBinding;
import waed.dev.adminhoria.models.Poster;
import waed.dev.adminhoria.utils.UtilsGeneral;


public class PrisonerPostersAdapter extends RecyclerView.Adapter<PrisonerPostersAdapter.PosterViewHolder> {
    private ArrayList<Poster> posters;

    private DeletePostersListListener deletePostersListListener;

    public void setDeletePrisonerPostersListListener(DeletePostersListListener deletePostersListListener) {
        this.deletePostersListListener = deletePostersListListener;
    }

    public PrisonerPostersAdapter() {
        this.posters = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Poster> posters) {
        this.posters = posters;
        notifyDataSetChanged();
    }

    public void removeItem(String id) {
        for (int i = 0; i < posters.size(); i++) {
            if (posters.get(i).getUuid().equals(id)) {
                posters.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPosterBinding binding = ItemPosterBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new PosterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        Poster poster = posters.get(position);
        holder.bind(poster);
        holder.setDeletePrisonerPostersListListener(deletePostersListListener);
    }

    @Override
    public int getItemCount() {
        return posters.size();
    }

    static class PosterViewHolder extends RecyclerView.ViewHolder {
        private final ItemPosterBinding binding;
        private final Context context;

        private DeletePostersListListener deletePostersListListener;

        public void setDeletePrisonerPostersListListener(DeletePostersListListener deletePostersListListener) {
            this.deletePostersListListener = deletePostersListListener;
        }

        public PosterViewHolder(ItemPosterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        public void bind(Poster poster) {
            UtilsGeneral.getInstance()
                    .loadImage(context, poster.getImageUrl())
                    .fitCenter()
                    .into(binding.ivPoster);

            binding.btnDeletePoster.setOnClickListener(v ->
                    deletePostersListListener.onClickDeleteListener(poster.getUuid(), poster.getImageUrl()));
        }
    }

    public interface DeletePostersListListener {
        void onClickDeleteListener(String prisonerPosterId, String imageUrl);
    }
}