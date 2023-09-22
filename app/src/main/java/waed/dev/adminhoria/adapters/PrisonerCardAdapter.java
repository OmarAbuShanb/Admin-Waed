package waed.dev.adminhoria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.databinding.ItemPrisonerCardBinding;
import waed.dev.adminhoria.models.PrisonerCard;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class PrisonerCardAdapter extends RecyclerView.Adapter<PrisonerCardAdapter.viewHolder> {
    private ArrayList<PrisonerCard> prisonerCards;
    private PrisonersCardsListListener prisonersCardsListListener;

    public void setNewsListCallback(PrisonersCardsListListener prisonersCardsListListener) {
        this.prisonersCardsListListener = prisonersCardsListListener;
    }

    public PrisonerCardAdapter() {
        this.prisonerCards = new ArrayList<>();
    }

    public void setPrisonerCards(ArrayList<PrisonerCard> prisonerCards) {
        this.prisonerCards = prisonerCards;
        notifyItemRangeInserted(0, prisonerCards.size());
    }

    public void removeItem(String id) {
        for (int i = 0; i < prisonerCards.size(); i++) {
            if (prisonerCards.get(i).getUuid().equals(id)) {
                prisonerCards.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPrisonerCardBinding binding = ItemPrisonerCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PrisonerCard model = prisonerCards.get(position);
        holder.bind(model);

        holder.setNewsListCallback(prisonersCardsListListener);
    }

    @Override
    public int getItemCount() {
        return prisonerCards.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        private final ItemPrisonerCardBinding binding;
        private final Context context;

        private PrisonersCardsListListener prisonersCardsListListener;

        public void setNewsListCallback(PrisonersCardsListListener prisonersCardsListListener) {
            this.prisonersCardsListListener = prisonersCardsListListener;
        }

        public viewHolder(@NonNull ItemPrisonerCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(PrisonerCard model) {
            binding.prisonerName.setText(model.getName());

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getImageUrl())
                    .into(binding.prisonerImage);

            binding.prisonerCard.setOnClickListener(v ->
                    prisonersCardsListListener.onClickItemListener(model));

            binding.btnDeletePrisonerCard.setOnClickListener(v ->
                    prisonersCardsListListener.onClickDeleteListener(model.getUuid(), model.getImageUrl()));
        }
    }

    public interface PrisonersCardsListListener {
        void onClickItemListener(PrisonerCard model);

        void onClickDeleteListener(String prisonerCardId, String imageUrl);
    }
}
