package waed.dev.adminhoria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.PrisonerCardItemBinding;
import waed.dev.adminhoria.models.PrisonerCard;

public class PrisonerCardAdapter extends RecyclerView.Adapter<PrisonerCardAdapter.viewHolder> {
    private ArrayList<PrisonerCard> prisonerCards;
    private PrisonersCardsListListener prisonersCardsListListener;
    private DeletePrisonersCardsListListener deletePrisonersCardsListListener;

    public void setNewsListCallback(PrisonersCardsListListener prisonersCardsListListener) {
        this.prisonersCardsListListener = prisonersCardsListListener;
    }

    public void setDeleteNewsCallback(DeletePrisonersCardsListListener deletePrisonersCardsListListener) {
        this.deletePrisonersCardsListListener = deletePrisonersCardsListListener;
    }


    public PrisonerCardAdapter(ArrayList<PrisonerCard> prisonerCards) {
        this.prisonerCards = prisonerCards;
    }

    public void setPrisonerCards(ArrayList<PrisonerCard> prisonerCards) {
        this.prisonerCards = prisonerCards;
        notifyItemRangeInserted(0, prisonerCards.size());
    }

    public void removeItem(int position) {
        prisonerCards.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PrisonerCardItemBinding binding
                = PrisonerCardItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PrisonerCard model = prisonerCards.get(position);
        holder.bind(model, position);

        holder.setNewsListCallback(prisonersCardsListListener);
        holder.setDeleteNewsCallback(deletePrisonersCardsListListener);
    }

    @Override
    public int getItemCount() {
        return prisonerCards.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        private final PrisonerCardItemBinding binding;
        private final Context context;

        private PrisonersCardsListListener prisonersCardsListListener;
        private DeletePrisonersCardsListListener deletePrisonersCardsListListener;

        protected void setNewsListCallback(PrisonersCardsListListener prisonersCardsListListener) {
            this.prisonersCardsListListener = prisonersCardsListListener;
        }

        protected void setDeleteNewsCallback(DeletePrisonersCardsListListener deletePrisonersCardsListListener) {
            this.deletePrisonersCardsListListener = deletePrisonersCardsListListener;
        }


        public viewHolder(@NonNull PrisonerCardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(PrisonerCard model, int position) {
            binding.prisonerName.setText(model.getName());

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getImageUrl())
                    .into(binding.prisonerImage);

            binding.prisonerCard.setOnClickListener(v ->
                    prisonersCardsListListener.onClickItemListener(model));

            binding.btnDeletePrisoner.setOnClickListener(v ->
                    deletePrisonersCardsListListener.onClickDeleteListener(model.getId(), position));
        }
    }

    public interface PrisonersCardsListListener {
        void onClickItemListener(PrisonerCard model);
    }

    public interface DeletePrisonersCardsListListener {
        void onClickDeleteListener(String prisonerCardId, int positionItem);
    }
}
