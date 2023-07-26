package waed.dev.adminhoria.screens.book;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ActivityAddBookBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Book;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class AddBookActivity extends AppCompatActivity {
    private ActivityAddBookBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Book newsModel;
    private Uri pdfUri = null;
    private Uri imageUri = null;
    private String bookName;
    private String bookAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        newsModel = (Book) getIntent().getSerializableExtra("model");
        if (newsModel != null) {
            putDate(newsModel);

            binding.btnAddBook.setOnClickListener(view -> performSetBook(true));
        } else {
            binding.btnAddBook.setOnClickListener(view -> performSetBook(false));
        }

        binding.buChoosePdfFile.setOnClickListener(view ->
                openDocumentLauncher.launch(new String[]{"application/pdf"}));

        binding.buChooseImageNews.setOnClickListener(view ->
                getContentLauncher.launch("image/*"));
    }

    private void putDate(Book book) {
        UtilsGeneral.getInstance()
                .loadImage(this, book.getImageUrl())
                .into(binding.ivImageBook);

        binding.buChoosePdfFile.setVisibility(View.GONE);
        binding.etBookName.setText(book.getName());
        binding.etAuthor.setText(book.getAuthor());
        binding.btnAddBook.setText("Update book");
    }

    private boolean checkData(String title, String description) {
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(description);
    }

    private void performSetBook(boolean updateBook) {
        bookName = Objects.requireNonNull(binding.etBookName.getText()).toString().trim();
        bookAuthor = Objects.requireNonNull(binding.etAuthor.getText()).toString().trim();

        if (checkData(bookName, bookAuthor)) {
            if (updateBook) {
                loadingDialog.show(getSupportFragmentManager(), "AddNews");
                if (imageUri != null) {
                    uploadImage(true, null);
                } else {
                    newsModel.setName(bookName);
                    newsModel.setAuthor(bookAuthor);
                    setBook(newsModel, newsModel.getId());
                }
            } else {
                if (imageUri != null && pdfUri != null) {
                    loadingDialog.show(getSupportFragmentManager(), "AddNews");
                    uploadPdfFile();
                }
            }
        }
    }

    private void uploadPdfFile() {
        firebaseController.uploadPdfFile(pdfUri, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String pdfUrl) {
                uploadImage(false, pdfUrl);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void uploadImage(boolean updateNews, String pdfUrl) {
        firebaseController.uploadImage(imageUri, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                if (updateNews) {
                    newsModel.setImageUrl(imageUrl);
                    newsModel.setName(bookName);
                    newsModel.setAuthor(bookAuthor);
                    setBook(newsModel, newsModel.getId());
                } else {
                    String id = UUID.randomUUID().toString();
                    Book book = new Book(id, imageUrl, bookName, bookAuthor, pdfUrl);
                    setBook(book, id);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setBook(Book book, String id) {
        firebaseController.setBook(book, id, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                UtilsGeneral.getInstance().showToast(getBaseContext(), "تمت العملية بنجاح");
                loadingDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private final ActivityResultLauncher<String[]> openDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(), result -> {
                if (result != null) {
                    pdfUri = result;
                    binding.tvChoosePdfFile.setText("1 File Chooses");
                }
            });

    private final ActivityResultLauncher<String> getContentLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            imageUri = result;
                            binding.ivImageBook.setImageURI(result);
                        }
                    }
            );
}