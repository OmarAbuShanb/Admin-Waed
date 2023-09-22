package waed.dev.adminhoria.screens.album_images;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.adapters.AlbumImagesAdapter;
import waed.dev.adminhoria.databinding.ActivityAlbumImagesBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.AlbumImage;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AlbumImagesActivity extends AppCompatActivity implements
        AlbumImagesAdapter.AlbumImageListListener,
        AlbumImagesAdapter.BaseAddAlbumImagesListener {
    private ActivityAlbumImagesBinding binding;
    private AlbumImagesAdapter albumImagesAdapter;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private ArrayList<Uri> newAlbumImagesUri;
    private ArrayList<AlbumImage> deleteAlbumImages;

    private String albumUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        newAlbumImagesUri = new ArrayList<>();
        deleteAlbumImages = new ArrayList<>();

        setupListeners();
        setupAlbumImagesAdapter();
        putDataIfExist();
    }

    private void putDataIfExist() {
        albumUuid = getIntent().getStringExtra("album_uuid");
        getAlbumImages();
    }

    private void setupListeners() {
        binding.floatAddAlbumsImages.setOnClickListener(view -> {
            if (newAlbumImagesUri.isEmpty() && deleteAlbumImages.isEmpty()) {
                UtilsGeneral.getInstance().showToast(this, getString(R.string.there_is_nothing_to_save));
            } else {
                loadingDialog.show(getSupportFragmentManager(), "AlbumImagesActivity");
                checkFollowingProcess();
            }
        });
    }

    private void setupAlbumImagesAdapter() {
        albumImagesAdapter = new AlbumImagesAdapter();
        binding.albumsImagesRecycler.setAdapter(albumImagesAdapter);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        binding.albumsImagesRecycler.setLayoutManager(manager);
        binding.albumsImagesRecycler.setHasFixedSize(true);

        albumImagesAdapter.setAlbumImageListCallback(this);
        albumImagesAdapter.setBaseAddAlbumImagesListener(this);
    }

    private void getAlbumImages() {
        binding.progressAlbumsImages.setVisibility(View.VISIBLE);
        firebaseController.getAlbumImages(albumUuid, new FirebaseController.GetDataCallback<>() {
            @Override
            public void onSuccess(ArrayList<AlbumImage> images) {
                binding.progressAlbumsImages.setVisibility(View.GONE);
                albumImagesAdapter.setData(images);
            }

            @Override
            public void onFailure(String errorMessage) {
                UtilsGeneral.getInstance()
                        .showToast(getBaseContext(), getString(R.string.something_went_wrong));
            }
        });
    }

    private void uploadAlbumImage(Uri imageUri) {
        firebaseController.uploadAlbumImages(albumUuid,imageUri, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                String newUuid = UUID.randomUUID().toString();
                AlbumImage newAlbumImage = new AlbumImage(newUuid, fileUrl, Timestamp.now());

                setAlbumImage(newAlbumImage, imageUri);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setAlbumImage(AlbumImage albumImage, Uri imageUri) {
        firebaseController.setAlbumImage(albumImage, albumUuid, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                newAlbumImagesUri.remove(imageUri);
                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteAlbumImage(String albumImageUuid, int itemIndex) {
        firebaseController.deleteAlbumImage(albumImageUuid, albumUuid, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deleteAlbumImages.remove(itemIndex);
                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void checkFollowingProcess() {
        if (!deleteAlbumImages.isEmpty()) {
            int itemIndex = deleteAlbumImages.size() - 1;
            String albumImageUuid = deleteAlbumImages.get(itemIndex).getUuid();
            deleteAlbumImage(albumImageUuid, itemIndex);
        } else if (!newAlbumImagesUri.isEmpty()) {
            int itemIndex = newAlbumImagesUri.size() - 1;
            Uri imageUri = newAlbumImagesUri.get(itemIndex);
            uploadAlbumImage(imageUri);
        } else {
            loadingDialog.dismiss();
            finish();
        }
    }

    private void addNewAlbumImages(List<Uri> uriList) {
        ArrayList<AlbumImage> albumImages = new ArrayList<>();
        Random random = new Random();
        for (Uri uri : uriList) {
            // randomNumberString => albumImagesAdapter.removeItem(model.getUuid());
            String randomNumberString = Integer.toString(random.nextInt(10000));
            albumImages.add(new AlbumImage(randomNumberString, uri));
        }
        albumImagesAdapter.addItems(albumImages);
    }

    private final ActivityResultLauncher<String> getAlbumImagesLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetMultipleContents(), result -> {
                        if (result != null && !result.isEmpty()) {
                            newAlbumImagesUri.addAll(result);
                            addNewAlbumImages(result);
                        }
                    });

    @Override
    public void onClickDeleteListener(AlbumImage model) {
        // delete new image
        if (model.getImageUri() != null) {
            newAlbumImagesUri.remove(model.getImageUri());
        } else {
            // delete old image
            deleteAlbumImages.add(model);
        }
        // refresh recycler
        albumImagesAdapter.removeItem(model.getUuid());
    }

    @Override
    public void onClickAddListener() {
        getAlbumImagesLauncher.launch("image/*");
    }
}