package waed.dev.adminhoria.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.databinding.ItemWhatsappTweetBinding;
import waed.dev.adminhoria.models.WhatsAppTweet;

public class WhatsAppTweetsAdapter extends RecyclerView.Adapter<WhatsAppTweetsAdapter.whatsappHolder> {
    private ArrayList<WhatsAppTweet> whatsAppTweets;

    public WhatsAppTweetsAdapter() {
        this.whatsAppTweets = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<WhatsAppTweet> whatsAppTweets) {
        this.whatsAppTweets = whatsAppTweets;
        notifyDataSetChanged();
    }

    public void addWhatsAppMessage(WhatsAppTweet message) {
        whatsAppTweets.add(message);
        notifyItemInserted(whatsAppTweets.size() - 1);
    }

    public String removeWhatsAppMessage(int position) {
        String uuid = whatsAppTweets.get(position).getUuid();
        whatsAppTweets.remove(position);
        notifyItemRemoved(position);
        return uuid;
    }

    @NonNull
    @Override
    public whatsappHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWhatsappTweetBinding binding = ItemWhatsappTweetBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new whatsappHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull whatsappHolder holder, int position) {
        WhatsAppTweet whatsAppTweet = whatsAppTweets.get(position);
        holder.bind(whatsAppTweet);
    }

    @Override
    public int getItemCount() {
        return whatsAppTweets.size();
    }

    static class whatsappHolder extends RecyclerView.ViewHolder {
        private final ItemWhatsappTweetBinding binding;

        public whatsappHolder(@NonNull ItemWhatsappTweetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(WhatsAppTweet whatsAppTweet) {
            binding.whatsAppTweet.setText(whatsAppTweet.getMessage());
        }
    }
}
