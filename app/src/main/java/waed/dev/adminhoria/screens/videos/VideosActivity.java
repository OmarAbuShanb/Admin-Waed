package waed.dev.adminhoria.screens.videos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.VideosAdapter;
import waed.dev.adminhoria.databinding.ActivityVideosBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Video;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class VideosActivity extends AppCompatActivity implements VideosAdapter.VideosListListener {
    private ActivityVideosBinding binding;
    private VideosAdapter videosAdapter;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setupListeners();
        setupVideosAdapter();
        getVideos();
    }

    private void setupListeners() {
        binding.floatAddVideos.setOnClickListener(view ->
                startActivity(new Intent(this, AddVideoActivity.class)));
    }

    private void setupVideosAdapter() {
        videosAdapter = new VideosAdapter();
        binding.videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.videosRecyclerView.setAdapter(videosAdapter);
        binding.videosRecyclerView.setHasFixedSize(true);

        videosAdapter.setVideosListCallback(this);
    }

    private void getVideos() {
        binding.progressVideos.setVisibility(View.VISIBLE);

        firebaseController.getVideos(new FirebaseController.GetDataCallback<>() {
            @Override
            public void onSuccess(ArrayList<Video> videos) {
                binding.progressVideos.setVisibility(View.GONE);

                videosAdapter.setData(videos);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void deleteVideo(Video video) {
        loadingDialog.show(getSupportFragmentManager(), "deleteVideo");
        firebaseController.deleteVideo(video.getUuid(), new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deleteVideoImage(video);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteVideoImage(Video video) {
        firebaseController.deleteFileUsingUrl(video.getVideoImageUrl(), new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deleteVideoFile(video);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteVideoFile(Video video) {
        firebaseController.deleteFileUsingUrl(video.getVideoUrl(), new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                videosAdapter.removeItem(video.getUuid());
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClickVideoItemListener(Video model) {
        Intent intent = new Intent(this, AddVideoActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }

    @Override
    public void onClickDeleteListener(Video video) {
        deleteVideo(video);
    }
}