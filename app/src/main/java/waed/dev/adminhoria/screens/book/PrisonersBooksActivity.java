package waed.dev.adminhoria.screens.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.PrisonerBooksAdapter;
import waed.dev.adminhoria.databinding.ActivityPrisonersBooksBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Book;
import waed.dev.adminhoria.models.News;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.screens.news.AddNewsActivity;
import waed.dev.adminhoria.screens.news.NewsActivity;

public class PrisonersBooksActivity extends AppCompatActivity
        implements PrisonerBooksAdapter.PrisonerBooksListListener,
        PrisonerBooksAdapter.DeletePrisonerBookListener {

    private ActivityPrisonersBooksBinding binding;
    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;
    private PrisonerBooksAdapter prisonerBooksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setupListeners();
        setupNewsAdapter();
        getBooks();
    }

    private void getBooks() {
        binding.progressBooks.setVisibility(View.VISIBLE);
        firebaseController.getBooks(new FirebaseController.GetBooksCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                binding.progressBooks.setVisibility(View.GONE);
                updateNewsAdapter(books);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void setupListeners() {
        binding.floatAddBook.setOnClickListener(view ->
                startActivity(new Intent(getBaseContext(), AddBookActivity.class)));
    }

    private void setupNewsAdapter() {
        prisonerBooksAdapter = new PrisonerBooksAdapter(new ArrayList<>());
        binding.booksRecyclerView.setAdapter(prisonerBooksAdapter);
        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.booksRecyclerView.setHasFixedSize(true);
    }

    private void updateNewsAdapter(ArrayList<Book> models) {
        prisonerBooksAdapter.setBooks(models);
        prisonerBooksAdapter.setPrisonerBooksListListener(this);
        prisonerBooksAdapter.setDeletePrisonerBookListener(this);
    }

    @Override
    public void onClickItemListener(Book model) {
        Intent intent = new Intent(getBaseContext(), AddBookActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }

    @Override
    public void onClickDeleteListener(String prisonerBookId, int positionItem) {
        loadingDialog.show(getSupportFragmentManager(), "deleteBook");
        firebaseController.deleteBook(prisonerBookId, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                prisonerBooksAdapter.removeItem(positionItem);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }
}