package waed.dev.adminhoria.screens.album;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import waed.dev.adminhoria.adapters.AlbumsAdapter;
import waed.dev.adminhoria.databinding.ActivityAlbumsBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Album;
import waed.dev.adminhoria.screens.album_images.AlbumImagesActivity;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;

public class AlbumsActivity extends AppCompatActivity implements AlbumsAdapter.AlbumsListListener {
    private ActivityAlbumsBinding binding;
    private AlbumsAdapter albumsAdapter;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        setUpListeners();
        setUpAlbumsAdapter();
        getAlbums();
    }

    private void setUpListeners() {
        binding.floatAddAlbums.setOnClickListener(view ->
                startActivity(new Intent(this, AddAlbumActivity.class)));
    }

    private void setUpAlbumsAdapter() {
        albumsAdapter = new AlbumsAdapter();
        binding.albumsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.albumsRecyclerView.setAdapter(albumsAdapter);
        binding.albumsRecyclerView.setHasFixedSize(true);

        albumsAdapter.setAlbumsListListener(this);
    }

    private void getAlbums() {
        binding.progressAlbums.setVisibility(View.VISIBLE);
        firebaseController.getAlbums(new FirebaseController.GetDataCallback<>() {
            @Override
            public void onSuccess(ArrayList<Album> albums) {
                binding.progressAlbums.setVisibility(View.GONE);
                albumsAdapter.setData(albums);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void deleteAlbum(Album album) {
        loadingDialog.show(getSupportFragmentManager(), "deleteAlbum");
        firebaseController.deleteAlbum(album.getUuid(), new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                deleteAlbumImage(album);
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteAlbumImage(Album album) {
        firebaseController.deleteFileUsingUrl(album.getImageUrl(), new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
                albumsAdapter.removeItem(album.getUuid());
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClickAlbumItemListener(String albumUuid) {
        Intent intent = new Intent(this, AlbumImagesActivity.class);
        intent.putExtra("album_uuid", albumUuid);
        startActivity(intent);
    }

    @Override
    public void onClickDeleteListener(Album album) {
        deleteAlbum(album);
    }

    @Override
    public void onClickUpdateListener(Album model) {
        Intent intent = new Intent(this, AddAlbumActivity.class);
        intent.putExtra("model", model);
        startActivity(intent);
    }
}