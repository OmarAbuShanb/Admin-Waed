package waed.dev.adminhoria.screens.book;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddPrisonerBookBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Book;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AddPrisonerBookActivity extends AppCompatActivity {
    private ActivityAddPrisonerBookBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Uri pdfUri, pdfImageUri;
    private String pdfName, pdfImageName;

    private String uuid, pdfUrl, imageUrl, bookName, bookAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPrisonerBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setUpListeners();
        putDateIfExist();
    }

    private void setUpListeners() {
        binding.buChoosePdfFile.setOnClickListener(view ->
                getPdfLauncher.launch(new String[]{"application/pdf"}));

        binding.buChooseImageNews.setOnClickListener(view ->
                getPdfImageLauncher.launch("image/*"));

        binding.btnAddBook.setOnClickListener(view -> performSetBook());
    }

    private void putDateIfExist() {
        Book book = (Book) getIntent().getSerializableExtra("model");
        if (book != null) {
            uuid = book.getUuid();
            pdfUrl = book.getPdfUrl();
            imageUrl = book.getImageUrl();

            UtilsGeneral.getInstance()
                    .loadImage(this, book.getImageUrl())
                    .into(binding.ivImageBook);

            binding.buChoosePdfFile.setVisibility(View.GONE);
            binding.etBookName.setText(book.getName());
            binding.etAuthor.setText(book.getAuthor());

            binding.headContentName.setText(R.string.update_the_book);
            binding.btnAddBook.setText(R.string.update);
        }
    }

    private boolean checkData(String title, String description) {
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) &&
                // If the user comes to add data
                ((pdfUri != null && pdfImageUri != null)
                        // Or comes to update data
                        || (pdfUrl != null && imageUrl != null));
    }

    private void performSetBook() {
        String bookName = Objects.requireNonNull(binding.etBookName.getText()).toString().trim();
        String bookAuthor = Objects.requireNonNull(binding.etAuthor.getText()).toString().trim();

        if (checkData(bookName, bookAuthor)) {
            this.bookName = bookName;
            this.bookAuthor = bookAuthor;

            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }

            loadingDialog.show(getSupportFragmentManager(), "AddPrisonerBookActivity");
            checkFollowingProcess();
        }
    }

    private void uploadPdfFile() {
        firebaseController.uploadBookFile(uuid, pdfUri, pdfName, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                pdfUri = null;
                pdfUrl = fileUrl;

                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void uploadImage() {
        firebaseController.uploadBookFile(uuid, pdfImageUri, pdfImageName, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                pdfImageUri = null;
                imageUrl = fileUrl;

                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setBook(Book book) {
        firebaseController.setBook(book, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                dismissDialogAndFinishSuccessfully();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void dismissDialogAndFinishSuccessfully() {
        UtilsGeneral.getInstance().showToast(getBaseContext(), getString(R.string.task_completed_successfully));
        loadingDialog.dismiss();
        finish();
    }

    private final ActivityResultLauncher<String[]> getPdfLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(), result -> {
                if (result != null) {
                    pdfUri = result;
                    pdfName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                    binding.tvChoosePdfFile.setText(R.string._1_file_chooses);
                }
            });

    private final ActivityResultLauncher<String> getPdfImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            pdfImageUri = result;
                            pdfImageName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                            binding.ivImageBook.setImageURI(result);
                        }
                    });

    private void checkFollowingProcess() {
        if (pdfUri != null) {
            uploadPdfFile();
        } else if (pdfImageUri != null) {
            uploadImage();
        } else {
            setBook(getStatistic());
        }
    }

    private Book getStatistic() {
        return new Book(uuid, imageUrl, bookName, bookAuthor, pdfUrl, Timestamp.now());
    }
}