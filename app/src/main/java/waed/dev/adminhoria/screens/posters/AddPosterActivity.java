package waed.dev.adminhoria.screens.posters;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import waed.dev.adminhoria.Utils.UtilsGeneral;
import waed.dev.adminhoria.databinding.ActivityAddPosterBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Poster;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class AddPosterActivity extends AppCompatActivity {
    private ActivityAddPosterBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPosterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        binding.buChoosePosterImage.setOnClickListener(view ->
                getContentLauncher.launch("image/*"));

        binding.btnAddPoster.setOnClickListener(view -> performSetPoster());
    }

    private final ActivityResultLauncher<String> getContentLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            imageUri = result;
                            binding.ivPoster.setImageURI(result);
                        }
                    }
            );

    private void performSetPoster() {
        if (imageUri != null) {
            loadingDialog.show(getSupportFragmentManager(), "setPoster");
            uploadImage();
        }
    }

    private void uploadImage() {
        firebaseController.uploadImage(imageUri, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                String id = UUID.randomUUID().toString();
                Poster poster = new Poster(id, imageUrl);
                setPoster(poster, id);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setPoster(Poster poster, String id) {
        firebaseController.setPoster(poster, id, new FirebaseController.FirebaseCallback() {
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
}