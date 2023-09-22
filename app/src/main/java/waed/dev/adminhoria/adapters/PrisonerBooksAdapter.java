package waed.dev.adminhoria.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ItemBooksBinding;
import waed.dev.adminhoria.models.Book;

public class PrisonerBooksAdapter extends RecyclerView.Adapter<PrisonerBooksAdapter.viewHolder> {
    private ArrayList<Book> books;
    private PrisonerBooksListListener prisonerBooksListListener;

    public PrisonerBooksAdapter() {
        books = new ArrayList<>();
    }

    public void setPrisonerBooksListListener(PrisonerBooksListListener prisonerBooksListListener) {
        this.prisonerBooksListListener = prisonerBooksListListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public void removeBook(String id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getUuid().equals(id)) {
                books.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBooksBinding binding = ItemBooksBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Book model = books.get(position);
        holder.bind(model);

        holder.setPrisonerBooksListListener(prisonerBooksListListener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        private final ItemBooksBinding binding;
        private final Context context;

        private PrisonerBooksListListener prisonerBooksListListener;

        public void setPrisonerBooksListListener(PrisonerBooksListListener prisonerBooksListListener) {
            this.prisonerBooksListListener = prisonerBooksListListener;
        }

        public viewHolder(@NonNull ItemBooksBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(Book model) {
            binding.bookName.setText(model.getName());
            binding.bookAuthor.setText(model.getAuthor());

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getImageUrl())
                    .into(binding.bookImage);

            binding.buBookCard.setOnClickListener(v ->
                    prisonerBooksListListener.onClickItemListener(model));

            binding.btnDeleteBook.setOnClickListener(v ->
                    prisonerBooksListListener.onClickDeleteListener(model));
        }
    }

    public interface PrisonerBooksListListener {
        void onClickItemListener(Book model);

        void onClickDeleteListener(Book model);
    }
}
