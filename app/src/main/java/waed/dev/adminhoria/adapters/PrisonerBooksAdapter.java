package waed.dev.adminhoria.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ItemBooksBinding;
import waed.dev.adminhoria.models.Book;

public class PrisonerBooksAdapter extends RecyclerView.Adapter<PrisonerBooksAdapter.viewHolder> {
    private ArrayList<Book> books;
    private PrisonerBooksListListener prisonerBooksListListener;
    private DeletePrisonerBookListener deletePrisonerBookListener;

    public void setPrisonerBooksListListener(PrisonerBooksListListener prisonerBooksListListener) {
        this.prisonerBooksListListener = prisonerBooksListListener;
    }

    public void setDeletePrisonerBookListener(DeletePrisonerBookListener deletePrisonerBookListener) {
        this.deletePrisonerBookListener = deletePrisonerBookListener;
    }

    public PrisonerBooksAdapter(ArrayList<Book> books) {
        this.books = books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyItemRangeInserted(0, books.size());
    }

    public void removeItem(int position) {
        books.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBooksBinding binding
                = ItemBooksBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Book model = books.get(position);
        holder.bind(model, position);
        holder.setPrisonerBooksListListener(prisonerBooksListListener);
        holder.setDeletePrisonerBookListener(deletePrisonerBookListener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        private final ItemBooksBinding binding;
        private final Context context;

        private PrisonerBooksListListener prisonerBooksListListener;
        private DeletePrisonerBookListener deletePrisonerBookListener;

        protected void setPrisonerBooksListListener(PrisonerBooksListListener prisonerBooksListListener) {
            this.prisonerBooksListListener = prisonerBooksListListener;
        }

        protected void setDeletePrisonerBookListener(DeletePrisonerBookListener deletePrisonerBookListener) {
            this.deletePrisonerBookListener = deletePrisonerBookListener;
        }

        public viewHolder(@NonNull ItemBooksBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            context = binding.getRoot().getContext();
        }

        void bind(Book model, int position) {
            binding.bookName.setText(model.getName());
            binding.bookAuthor.setText(model.getAuthor());

            UtilsGeneral.getInstance()
                    .loadImage(context, model.getImageUrl())
                    .into(binding.bookImage);

            binding.buBookCard.setOnClickListener(v -> prisonerBooksListListener.onClickItemListener(model));

            binding.btnDeleteBook.setOnClickListener(v ->
                    deletePrisonerBookListener.onClickDeleteListener(model.getId(), position));
        }
    }

    public interface PrisonerBooksListListener {
        void onClickItemListener(Book model);
    }

    public interface DeletePrisonerBookListener {
        void onClickDeleteListener(String prisonerBookId, int positionItem);
    }
}
