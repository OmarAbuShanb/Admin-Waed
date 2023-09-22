package waed.dev.adminhoria.screens.album;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddToAlbumBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Album;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AddAlbumActivity extends AppCompatActivity {
    private ActivityAddToAlbumBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Uri imageUri;
    private String imageName;

    private String uuid, imageUrl, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddToAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setUpListener();
        putDataIfExist();
    }

    private void setUpListener() {
        binding.buChooseImageAlbum.setOnClickListener(view ->
                getAlbumImageLauncher.launch("image/*"));

        binding.btnAddAlbum.setOnClickListener(v -> performSetAlbum());
    }

    private void putDataIfExist() {
        Album albumModel = (Album) getIntent().getSerializableExtra("model");
        if (albumModel != null) {
            uuid = albumModel.getUuid();
            imageUrl = albumModel.getImageUrl();

            UtilsGeneral.getInstance()
                    .loadImage(this, albumModel.getImageUrl())
                    .into(binding.ivAlbumImage);

            binding.etAlbumTitle.setText(albumModel.getTitle());

            binding.headContentName.setText(R.string.update_album);
            binding.btnAddAlbum.setText(R.string.update);
        }
    }

    private boolean checkData(String title) {
        return !TextUtils.isEmpty(title) &&
                // If the user comes to add data or comes to update data
                (imageUri != null || imageUrl != null);
    }

    private void performSetAlbum() {
        String title = Objects.requireNonNull(binding.etAlbumTitle.getText()).toString().trim();

        if (checkData(title)) {
            loadingDialog.show(getSupportFragmentManager(), "setAlbum");

            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }

            this.title = title;
            checkFollowingProcess();
        }
    }

    private void uploadImage() {
        firebaseController.uploadAlbumImage(uuid, imageUri, imageName, new FirebaseController.UploadFileCallback() {
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

    private void setAlbum(Album album) {
        firebaseController.setAlbum(album, new FirebaseController.FirebaseCallback() {
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

    private final ActivityResultLauncher<String> getAlbumImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            imageUri = result;
                            imageName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                            binding.ivAlbumImage.setImageURI(result);
                        }
                    }
            );

    private void checkFollowingProcess() {
        if (imageUri != null) {
            uploadImage();
        } else {
            setAlbum(getAlbum());
        }
    }

    private Album getAlbum() {
        return new Album(uuid, imageUrl, title, Timestamp.now());
    }
}