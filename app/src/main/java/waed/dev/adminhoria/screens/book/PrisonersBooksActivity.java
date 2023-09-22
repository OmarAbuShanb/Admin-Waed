package waed.dev.adminhoria.screens.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.PrisonerBooksAdapter;
import waed.dev.adminhoria.databinding.ActivityPrisonersBooksBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Book;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class PrisonersBooksActivity extends AppCompatActivity
        implements PrisonerBooksAdapter.PrisonerBooksListListener {

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

    private void setupListeners() {
        binding.floatAddBook.setOnClickListener(view ->
                startActivity(new Intent(getBaseContext(), AddPrisonerBookActivity.class)));
    }

    private void setupNewsAdapter() {
        prisonerBooksAdapter = new PrisonerBooksAdapter();
        binding.booksRecyclerView.setAdapter(prisonerBooksAdapter);
        RecyclerView.LayoutManager manager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.booksRecyclerView.setLayoutManager(manager);
        binding.booksRecyclerView.setHasFixedSize(true);

        prisonerBooksAdapter.setPrisonerBooksListListener(this);
    }

    private void getBooks() {
        binding.progressBooks.setVisibility(View.VISIBLE);
        firebaseController.getBooks(new FirebaseController.GetDataCallback<>() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                binding.progressBooks.setVisibility(View.GONE);
                prisonerBooksAdapter.setData(books);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void deleteBook(Book book) {
        loadingDialog.show(getSupportFragmentManager(), "deleteBook");
        firebaseController.deleteBook(book.getUuid(), new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deleteBookImage(book);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteBookImage(Book book) {
        firebaseController.deleteFileUsingUrl(book.getImageUrl(), new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deleteBookPdf(book);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteBookPdf(Book book) {
        firebaseController.deleteFileUsingUrl(book.getPdfUrl(), new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                prisonerBooksAdapter.removeBook(book.getUuid());
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClickItemListener(Book model) {
        Intent intent = new Intent(this, AddPrisonerBookActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }

    @Override
    public void onClickDeleteListener(Book book) {
        deleteBook(book);
    }
}