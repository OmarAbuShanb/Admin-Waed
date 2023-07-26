package waed.dev.adminhoria.screens.news;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ActivityAddNewsBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.News;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class AddNewsActivity extends AppCompatActivity {
    private static final String TAG = "AddNewsActivity";

    private ActivityAddNewsBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private News newsModel;
    private Uri imageUri = null;
    private String title;
    private String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        newsModel = (News) getIntent().getSerializableExtra("model");
        if (newsModel != null) {
            putDate(newsModel);

            binding.btnAddNews.setOnClickListener(view -> performSetNews(true));
        } else {
            binding.btnAddNews.setOnClickListener(view -> performSetNews(false));
        }

        binding.buChooseImageNews.setOnClickListener(view ->
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    private void putDate(News news) {
        UtilsGeneral.getInstance()
                .loadImage(this, news.getImageUrl())
                .into(binding.ivImageNews);

        binding.etTitle.setText(news.getTitle());
        binding.etDetails.setText(news.getDetails());
        binding.btnAddNews.setText("Update news");
    }

    private boolean checkData(String title, String description) {
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(description);
    }

    private void performSetNews(boolean updateNews) {
        title = Objects.requireNonNull(binding.etTitle.getText()).toString().trim();
        details = Objects.requireNonNull(binding.etDetails.getText()).toString().trim();

        if (checkData(title, details)) {
            loadingDialog.show(getSupportFragmentManager(), "AddNews");

            if (updateNews) {
                if (imageUri != null) {
                    uploadImage(true);
                } else {
                    newsModel.setTitle(title);
                    newsModel.setDetails(details);
                    setNews(newsModel, newsModel.getId());
                }
            } else {
                if (imageUri != null) {
                    uploadImage(false);
                }
            }
        }
    }

    private void uploadImage(boolean updateNews) {
        firebaseController.uploadImage(imageUri, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                if (updateNews) {
                    newsModel.setImageUrl(imageUrl);
                    newsModel.setTitle(title);
                    newsModel.setDetails(details);
                    setNews(newsModel, newsModel.getId());
                } else {
                    String id = UUID.randomUUID().toString();
                    News news = new News(id, imageUrl, title, details);
                    setNews(news, id);
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setNews(News news, String id) {
        firebaseController.setNews(news, id, new FirebaseController.FirebaseCallback() {
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

    private final ActivityResultLauncher<String> getContentLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            imageUri = result;
                            binding.ivImageNews.setImageURI(result);
                        }
                    }
            );

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getContentLauncher.launch("image/*");
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Log.d(TAG, "requestPermissionLauncher: " + Manifest.permission.READ_EXTERNAL_STORAGE + "  DENIED");
                    } else {
                        Log.d(TAG, "requestPermissionLauncher: " + Manifest.permission.READ_EXTERNAL_STORAGE + "  PERMANENTLY DENIED");
                    }
                }
            });
}