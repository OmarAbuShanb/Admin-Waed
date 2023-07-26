package waed.dev.adminhoria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ItemPosterBinding;
import waed.dev.adminhoria.models.Poster;


public class PrisonerPostersAdapter extends RecyclerView.Adapter<PrisonerPostersAdapter.PosterViewHolder> {
    private ArrayList<Poster> posters;

    private DeletePostersListListener deletePostersListListener;

    public void setDeletePrisonerPostersListListener(DeletePostersListListener deletePostersListListener) {
        this.deletePostersListListener = deletePostersListListener;
    }

    public PrisonerPostersAdapter(ArrayList<Poster> posters) {
        this.posters = posters;
    }

    public void setPosters(ArrayList<Poster> posters) {
        this.posters = posters;
        notifyItemRangeInserted(0, posters.size());
    }

    public void removeItem(int position) {
        posters.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPosterBinding binding
                = ItemPosterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PosterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        Poster poster = posters.get(position);
        holder.bind(poster,position);

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

        protected void setDeletePrisonerPostersListListener(DeletePostersListListener deletePostersListListener) {
            this.deletePostersListListener = deletePostersListListener;
        }

        public PosterViewHolder(ItemPosterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        public void bind(Poster poster, int position) {
            UtilsGeneral.getInstance()
                    .loadImage(context, poster.getImageUrl())
                    .fitCenter()
                    .into(binding.ivPoster);

            binding.btnDeleteBook.setOnClickListener(v ->
                    deletePostersListListener.onClickDeleteListener(poster.getId(), position));
        }
    }

    public interface DeletePostersListListener {
        void onClickDeleteListener(String prisonerPosterId, int positionItem);
    }
}