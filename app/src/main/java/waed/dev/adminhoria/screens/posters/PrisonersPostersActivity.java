package waed.dev.adminhoria.screens.posters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.PrisonerPostersAdapter;
import waed.dev.adminhoria.databinding.ActivityPrisonersPostersBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Poster;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class PrisonersPostersActivity extends AppCompatActivity
        implements PrisonerPostersAdapter.DeletePostersListListener {

    private ActivityPrisonersPostersBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;
    private PrisonerPostersAdapter prisonerPostersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersPostersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setupListeners();
        setupPrisonerPostersAdapter();
        getPosters();
    }

    private void setupListeners() {
        binding.floatAddPoster.setOnClickListener(view ->
                startActivity(new Intent(getBaseContext(), AddPrisonerPosterActivity.class)));
    }

    private void setupPrisonerPostersAdapter() {
        prisonerPostersAdapter = new PrisonerPostersAdapter();
        binding.postersPager.setAdapter(prisonerPostersAdapter);

        prisonerPostersAdapter.setDeletePrisonerPostersListListener(this);
    }

    private void getPosters() {
        binding.progressPrisonersPosters.setVisibility(View.VISIBLE);
        firebaseController.getPosters(new FirebaseController.GetDataCallback<>() {
            @Override
            public void onSuccess(ArrayList<Poster> posters) {
                binding.progressPrisonersPosters.setVisibility(View.GONE);
                prisonerPostersAdapter.setData(posters);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void deletePoster(String prisonerPosterId, String imageUrl) {
        loadingDialog.show(getSupportFragmentManager(), "deletePoster");
        firebaseController.deletePoster(prisonerPosterId, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deletePosterImage(prisonerPosterId, imageUrl);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deletePosterImage(String prisonerPosterId, String imageUrl) {
        firebaseController.deleteFileUsingUrl(imageUrl, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                prisonerPostersAdapter.removeItem(prisonerPosterId);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClickDeleteListener(String prisonerPosterId, String imageUrl) {
        deletePoster(prisonerPosterId, imageUrl);
    }
}