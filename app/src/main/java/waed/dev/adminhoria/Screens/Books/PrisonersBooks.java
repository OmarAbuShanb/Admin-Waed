package waed.dev.adminhoria.Screens.Books;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.Adapters.PrisonerBooksAdapter;
import waed.dev.adminhoria.Models.Book;
import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityPrisonersBooksBinding;

public class PrisonersBooks extends AppCompatActivity {
    ActivityPrisonersBooksBinding binding;

    PrisonerBooksAdapter booksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        booksAdapter = new PrisonerBooksAdapter(books);
        binding.booksRecyclerView.setAdapter(booksAdapter);
        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.booksRecyclerView.setHasFixedSize(true);

        binding.floatAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrisonersBooks.this, AddBook.class));
            }
        });
    }
}