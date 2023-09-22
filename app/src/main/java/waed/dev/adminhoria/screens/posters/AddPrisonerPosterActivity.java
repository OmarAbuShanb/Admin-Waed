package waed.dev.adminhoria.screens.posters;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddPrisonerPosterBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Poster;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AddPrisonerPosterActivity extends AppCompatActivity {
    private ActivityAddPrisonerPosterBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Uri imageUri;
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPrisonerPosterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setUpListener();
    }

    private void setUpListener() {
        binding.buChoosePosterImage.setOnClickListener(view ->
                getPosterImageLauncher.launch("image/*"));

        binding.btnAddPoster.setOnClickListener(view -> performSetPoster());
    }

    private void performSetPoster() {
        if (imageUri != null) {
            loadingDialog.show(getSupportFragmentManager(), "AddPrisonerPosterActivity");
            uploadImage();
        }
    }

    private void uploadImage() {
        firebaseController.uploadPosterImage(imageUri, imageName, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                setPoster(getPoster(imageUrl));
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setPoster(Poster poster) {
        firebaseController.setPoster(poster, new FirebaseController.FirebaseCallback() {
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

    private Poster getPoster(String imageUrl) {
        String id = UUID.randomUUID().toString();
        return new Poster(id, imageUrl, Timestamp.now());
    }

    private void dismissDialogAndFinishSuccessfully() {
        UtilsGeneral.getInstance().showToast(getBaseContext(), getString(R.string.task_completed_successfully));
        loadingDialog.dismiss();
        finish();
    }

    private final ActivityResultLauncher<String> getPosterImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            imageUri = result;
                            imageName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                            binding.ivPoster.setImageURI(result);
                        }
                    }
            );
}