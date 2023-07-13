package waed.dev.adminhoria.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.Models.PrisonerCard;
import waed.dev.adminhoria.databinding.PrisonerCardItemBinding;

public class PrisonerCardAdapter extends RecyclerView.Adapter<PrisonerCardAdapter.viewHolder> {
    ArrayList<PrisonerCard> prisonerCards;

    public PrisonerCardAdapter(ArrayList<PrisonerCard> prisonerCards) {
        this.prisonerCards = prisonerCards;
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
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return prisonerCards.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        private final PrisonerCardItemBinding binding;
        private final Context context;

        public viewHolder(@NonNull PrisonerCardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(PrisonerCard model) {
            binding.prisonerImage.setImageResource(model.getImageUrl());
            binding.prisonerName.setText(model.getName());
        }
    }
}
