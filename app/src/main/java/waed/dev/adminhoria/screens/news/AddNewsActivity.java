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

import com.google.firebase.Timestamp;

import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddNewsBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.News;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AddNewsActivity extends AppCompatActivity {
    private static final String TAG = "AddNewsActivity";

    private ActivityAddNewsBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Uri imageUri;
    private String imageName;

    private String uuid, imageUrl, title, details;

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

        setUpListener();
        putDate();
    }

    private void setUpListener() {
        binding.btnAddNews.setOnClickListener(view -> performSetNews());

        binding.buChooseImageNews.setOnClickListener(view ->
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    private void putDate() {
        News newsModel = (News) getIntent().getSerializableExtra("model");
        if (newsModel != null) {
            uuid = newsModel.getUuid();
            imageUrl = newsModel.getImageUrl();

            UtilsGeneral.getInstance()
                    .loadImage(this, newsModel.getImageUrl())
                    .into(binding.ivImageNews);

            binding.etTitle.setText(newsModel.getTitle());
            binding.etDetails.setText(newsModel.getDetails());

            binding.btnAddNews.setText(R.string.update_the_news);
            binding.btnAddNews.setText(R.string.update);
        }
    }

    private boolean checkData(String title, String description) {
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)
                // If the user comes to add data
                && (imageUri != null
                // Or comes to update data
                || imageUrl != null);
    }

    private void performSetNews() {
        String title = Objects.requireNonNull(binding.etTitle.getText()).toString().trim();
        String details = Objects.requireNonNull(binding.etDetails.getText()).toString().trim();

        if (checkData(title, details)) {
            this.title = title;
            this.details = details;

            // If the user comes to add data
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }

            loadingDialog.show(getSupportFragmentManager(), "AddNewsActivity");
            checkFollowingProcess();
        }
    }

    private void uploadImage() {
        firebaseController.uploadNewsImage(imageUri, imageName, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                imageUri = null;
                imageUrl = fileUrl;

                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setNews(News news) {
        firebaseController.setNews(news, new FirebaseController.FirebaseCallback() {
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

    private final ActivityResultLauncher<String> getNewsImage =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            imageUri = result;
                            imageName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                            binding.ivImageNews.setImageURI(result);
                        }
                    }
            );

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    getNewsImage.launch("image/*");
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Log.d(TAG, "requestPermissionLauncher: " + Manifest.permission.READ_EXTERNAL_STORAGE + "  DENIED");
                    } else {
                        Log.d(TAG, "requestPermissionLauncher: " + Manifest.permission.READ_EXTERNAL_STORAGE + "  PERMANENTLY DENIED");
                    }
                }
            });

    private void checkFollowingProcess() {
        if (imageUri != null) {
            uploadImage();
        } else {
            setNews(getNews());
        }
    }

    private News getNews() {
        return new News(uuid, imageUrl, title, details, Timestamp.now());
    }
}