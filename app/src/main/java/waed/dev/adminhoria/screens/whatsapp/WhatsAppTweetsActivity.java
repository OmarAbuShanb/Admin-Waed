package waed.dev.adminhoria.screens.whatsapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.adapters.WhatsAppTweetsAdapter;
import waed.dev.adminhoria.databinding.ActivityWhatsappTweetsBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.WhatsAppTweet;
import waed.dev.adminhoria.screens.dialogs.GetTextDialog;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class WhatsAppTweetsActivity extends AppCompatActivity {
    private ActivityWhatsappTweetsBinding binding;
    private WhatsAppTweetsAdapter whatsAppTweetsAdapter;

    private FirebaseController firebaseController;

    private FragmentManager fragmentManager;
    private LoadingDialog loadingDialog;
    private GetTextDialog updateWhatsAppGroupUrlDialog;
    private GetTextDialog setWhatsAppTweetsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhatsappTweetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();

        setUpListeners();
        setUpDialogs();
        setUpWhatsAppTweetsAdapter();
        attachItemTouchHelperToRecycler();
        getWhatsAppTweets();
    }

    private void setUpDialogs() {
        fragmentManager = getSupportFragmentManager();
        loadingDialog = new LoadingDialog();

        setWhatsAppTweetsDialog = new GetTextDialog(
                R.string.add_new_tweet,
                R.string.enter_tweet_text,
                R.string.add,
                (GetTextDialog.ClickDialogButtonListener) message -> {
                    String uuid = UUID.randomUUID().toString();
                    WhatsAppTweet whatsAppTweet = new WhatsAppTweet(uuid, message, Timestamp.now());
                    setWhatsAppTweets(whatsAppTweet);
                }
        );

        updateWhatsAppGroupUrlDialog = new GetTextDialog(
                R.string.update_url_whatsapp_group,
                R.string.enter_the_new_url,
                R.string.update,
                (GetTextDialog.ClickDialogButtonListener) this::updateWhatsAppGroupUrl
        );
    }

    private void setUpListeners() {
        binding.floatPushTweets.setOnClickListener(view ->
                setWhatsAppTweetsDialog.show(fragmentManager, "setWhatsAppTweetsDialog"));

        binding.floatUpdateWhatsAppGroupUrl.setOnClickListener(view ->
                updateWhatsAppGroupUrlDialog.show(fragmentManager, "updateWhatsAppGroupUrlDialog")
        );
    }

    private void setUpWhatsAppTweetsAdapter() {
        whatsAppTweetsAdapter = new WhatsAppTweetsAdapter();
        binding.whatsappTweetsRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.whatsappTweetsRecycler.setAdapter(whatsAppTweetsAdapter);
        binding.whatsappTweetsRecycler.setHasFixedSize(true);
    }

    private void attachItemTouchHelperToRecycler() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String whatsAppTweetUuid = whatsAppTweetsAdapter.removeWhatsAppMessage(position);

                deleteWhatsAppTweets(whatsAppTweetUuid);
            }
        }).attachToRecyclerView(binding.whatsappTweetsRecycler);
    }

    private void getWhatsAppTweets() {
        firebaseController.getWhatsAppTweets(new FirebaseController.GetDataCallback<>() {
            @Override
            public void onSuccess(ArrayList<WhatsAppTweet> whatsAppTweets) {
                binding.progressWhatsAppMessages.setVisibility(View.GONE);
                whatsAppTweetsAdapter.setData(whatsAppTweets);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void setWhatsAppTweets(WhatsAppTweet whatsAppTweet) {
        loadingDialog.show(fragmentManager, "setWhatsAppTweets");
        firebaseController.setWhatsAppTweets(whatsAppTweet, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                dismissDialogAndWhatsUpMessageSuccessfully(whatsAppTweet);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void dismissDialogAndWhatsUpMessageSuccessfully(WhatsAppTweet whatsAppTweet) {
        loadingDialog.dismiss();
        UtilsGeneral.getInstance().showToast(getBaseContext(), getString(R.string.task_completed_successfully));
        whatsAppTweetsAdapter.addWhatsAppMessage(whatsAppTweet);
    }

    private void deleteWhatsAppTweets(String whatsAppTweetUuid) {
        loadingDialog.show(fragmentManager, "deleteWhatsAppTweets");
        firebaseController.deleteWhatsAppTweets(whatsAppTweetUuid, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void updateWhatsAppGroupUrl(String newUrl) {
        loadingDialog.show(fragmentManager, "updateWhatsAppGroupUrl");
        firebaseController.updateWhatsAppGroupUrl(newUrl, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                UtilsGeneral.getInstance().showToast(getBaseContext(), getString(R.string.join_url_updated));
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                UtilsGeneral.getInstance().showToast(getBaseContext(), getString(R.string.something_went_wrong));
                loadingDialog.dismiss();
            }
        });
    }
}