package waed.dev.adminhoria.screens.book;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.PrisonerBooksAdapter;
import waed.dev.adminhoria.models.Book;
import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityPrisonersBooksBinding;

public class PrisonersBooksActivity extends AppCompatActivity {
    private ActivityPrisonersBooksBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }
    private void init(){
        setupListeners();
        setupRecyclerView(createDummyData());
    }


    private void setupListeners() {
        binding.floatAddBook.setOnClickListener(view -> startActivity(new Intent(PrisonersBooksActivity.this, AddBookActivity.class)));
    }
    private void setupRecyclerView(ArrayList<Book> books) {
        PrisonerBooksAdapter booksAdapter = new PrisonerBooksAdapter(books);
        binding.booksRecyclerView.setAdapter(booksAdapter);
        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.booksRecyclerView.setHasFixedSize(true);
    }
    private ArrayList<Book> createDummyData() {
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        books.add(new Book("asdofjsadf",R.drawable.test_image_book, "العاصي", "سائد سلامة"));
        return books;
    }

}