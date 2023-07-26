package waed.dev.adminhoria.screens.posters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import waed.dev.adminhoria.Utils.RotationPageTransformer;
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
                startActivity(new Intent(getBaseContext(), AddPosterActivity.class)));
    }

    private void getPosters() {
        binding.progressPrisonersPosters.setVisibility(View.VISIBLE);
        firebaseController.getPosters(new FirebaseController.GetPostersCallback() {
            @Override
            public void onSuccess(ArrayList<Poster> posters) {
                binding.progressPrisonersPosters.setVisibility(View.GONE);
                updatePrisonerPostersAdapter(posters);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void setupPrisonerPostersAdapter() {
        prisonerPostersAdapter = new PrisonerPostersAdapter(new ArrayList<>());
        binding.postersPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        binding.postersPager.setPageTransformer(new RotationPageTransformer(160));
        binding.postersPager.setAdapter(prisonerPostersAdapter);
    }

    private void updatePrisonerPostersAdapter(ArrayList<Poster> models) {
        prisonerPostersAdapter.setPosters(models);
        prisonerPostersAdapter.setDeletePrisonerPostersListListener(this);
    }


    @Override
    public void onClickDeleteListener(String prisonerPosterId, int positionItem) {
        loadingDialog.show(getSupportFragmentManager(), "deletePoster");
        firebaseController.deletePoster(prisonerPosterId, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                prisonerPostersAdapter.removeItem(positionItem);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }
}