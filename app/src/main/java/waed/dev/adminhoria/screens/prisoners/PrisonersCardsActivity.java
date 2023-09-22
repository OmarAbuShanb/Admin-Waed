package waed.dev.adminhoria.screens.prisoners;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.PrisonerCardAdapter;
import waed.dev.adminhoria.databinding.ActivityPrisonersCardsBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.PrisonerCard;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class PrisonersCardsActivity extends AppCompatActivity
        implements PrisonerCardAdapter.PrisonersCardsListListener {
    private ActivityPrisonersCardsBinding binding;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;
    private PrisonerCardAdapter prisonerCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrisonersCardsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setupListeners();
        setupPrisonerCardsAdapter();
        getPrisonersCards();
    }

    private void setupListeners() {
        binding.floatAddPrisoner.setOnClickListener(view ->
                startActivity(new Intent(getBaseContext(), AddPrisonerCardActivity.class)));
    }

    private void setupPrisonerCardsAdapter() {
        prisonerCardAdapter = new PrisonerCardAdapter();
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        binding.prisonersCardsRecyclerView.setLayoutManager(manager);
        binding.prisonersCardsRecyclerView.setAdapter(prisonerCardAdapter);
        binding.prisonersCardsRecyclerView.setHasFixedSize(true);

        prisonerCardAdapter.setNewsListCallback(this);
    }

    private void getPrisonersCards() {
        binding.progressPrisonersCards.setVisibility(View.VISIBLE);
        firebaseController.getPrisonersCards(new FirebaseController.GetDataCallback<>() {
            @Override
            public void onSuccess(ArrayList<PrisonerCard> prisonerCards) {
                binding.progressPrisonersCards.setVisibility(View.GONE);
                prisonerCardAdapter.setPrisonerCards(prisonerCards);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void deletePrisonerCard(String prisonerCardId, String imageUrl) {
        loadingDialog.show(getSupportFragmentManager(), "deletePrisonerCard");
        firebaseController.deletePrisonerCard(prisonerCardId, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deletePrisonerImage(prisonerCardId, imageUrl);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deletePrisonerImage(String prisonerCardId, String imageUrl) {
        firebaseController.deleteFileUsingUrl(imageUrl, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                prisonerCardAdapter.removeItem(prisonerCardId);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClickItemListener(PrisonerCard model) {
        Intent intent = new Intent(getBaseContext(), AddPrisonerCardActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }

    @Override
    public void onClickDeleteListener(String prisonerCardId, String imageUrl) {
        deletePrisonerCard(prisonerCardId, imageUrl);
    }

}