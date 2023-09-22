package waed.dev.adminhoria.screens.videos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.Timestamp;

import java.util.Objects;
import java.util.UUID;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.UploadVideosService;
import waed.dev.adminhoria.databinding.ActivityAddVideoBinding;
import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Video;
import waed.dev.adminhoria.screens.dialogs.LoadingDialog;
import waed.dev.adminhoria.utils.UtilsGeneral;

public class AddVideoActivity extends AppCompatActivity {
    private static final String TAG = "AddVideoActivity";

    private ActivityAddVideoBinding binding;

    private BroadcastReceiver mBroadcastReceiver;

    private FirebaseController firebaseController;
    private LoadingDialog loadingDialog;

    private Uri videoUri, videoImageUri;
    private Bitmap videoImageBitmap;

    private String videoName, videoImageName;

    private String uuid, videoUrl, videoImageUrl, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        firebaseController = FirebaseController.getInstance();
        loadingDialog = new LoadingDialog();

        putDataIfExist();
        setUpListener();
        setUpBroadcastReceiver();
    }

    private void setUpBroadcastReceiver() {
        // Local broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);

                switch (Objects.requireNonNull(intent.getAction())) {
                    case UploadVideosService.UPLOAD_COMPLETED, UploadVideosService.UPLOAD_ERROR ->
                            onUploadResultIntent(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();

        // Register receiver for uploads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, UploadVideosService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();

        // Unregister uploads receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private void uploadVideoInForeground() {
        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        Intent intentService = new Intent(this, UploadVideosService.class)
                .putExtra(UploadVideosService.EXTRA_VIDEO_URI, videoUri)
                .putExtra(UploadVideosService.EXTRA_VIDEO_NAME, videoName)
                // getVideo() => videoUrl is null
                .putExtra(UploadVideosService.EXTRA_VIDEO_MODEL, getVideo())
                .setAction(UploadVideosService.ACTION_UPLOAD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intentService);
        } else {
            ContextCompat.startForegroundService(this, intentService);
        }
        dismissDialogAndFinishSuccessfully(getString(R.string.video_uploading_in_background));
    }

    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
//        mFileUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);
    }

    private void putDataIfExist() {
        Video videoModel = (Video) getIntent().getSerializableExtra("model");
        if (videoModel != null) {
            uuid = videoModel.getUuid();
            videoUrl = videoModel.getVideoUrl();
            videoImageUrl = videoModel.getVideoImageUrl();

            UtilsGeneral.getInstance()
                    .loadImage(this, videoModel.getVideoImageUrl())
                    .into(binding.ivVideoImage);

            binding.tvChooseVideo.setText(R.string._1_file_chooses);
            binding.etTitle.setText(videoModel.getTitle());

            binding.headContentName.setText(R.string.update_the_video_data);
            binding.btnAddVideo.setText(R.string.update);
        }
    }

    private void setUpListener() {
        binding.buChooseVideo.setOnClickListener(view ->
                getVideoLauncher.launch("video/*"));

        binding.buChooseVideoImage.setOnClickListener(view ->
                getVideoImageLauncher.launch("image/*"));

        binding.btnAddVideo.setOnClickListener(v -> performSetVideo());
    }

    private boolean checkData(String title) {
        return !TextUtils.isEmpty(title) &&
                // If the user comes to add data
                // The user must have chosen either videoImageUri or videoImageBitmap
                (((videoImageUri != null || videoImageBitmap != null) && videoUri != null)
                        // Or comes to update data
                        || (videoImageUrl != null && videoUrl != null));
    }

    private void performSetVideo() {
        String title = Objects.requireNonNull(binding.etTitle.getText()).toString().trim();

        if (checkData(title)) {
            this.title = title;

            // If the user comes to add data
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }

            loadingDialog.show(getSupportFragmentManager(), "uploadVideo");
            checkFollowingProcess();
        }
    }

    private void uploadVideoImage() {
        firebaseController.uploadVideoFile(uuid, videoImageUri, videoImageName, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                videoImageUri = null;
                videoImageUrl = fileUrl;

                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void uploadVideoImageBitmap() {
        firebaseController.uploadVideoImageBitmap(uuid, videoImageBitmap, new FirebaseController.UploadFileCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                videoImageBitmap = null;
                videoImageUrl = fileUrl;

                checkFollowingProcess();
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void setVideo(Video video) {
        firebaseController.setVideo(video, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                dismissDialogAndFinishSuccessfully(getString(R.string.task_completed_successfully));
            }

            @Override
            public void onFailure(String errorMessage) {
                loadingDialog.dismiss();
            }
        });
    }

    private void dismissDialogAndFinishSuccessfully(String toastMessage) {
        UtilsGeneral.getInstance().showToast(getBaseContext(), toastMessage);
        loadingDialog.dismiss();
        finish();
    }

    private final ActivityResultLauncher<String> getVideoLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            videoUri = result;

                            videoName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                            setThumbnailVideoIfAllow(result);
                            binding.tvChooseVideo.setText(R.string._1_file_chooses);
                        }
                    }
            );

    private void setThumbnailVideoIfAllow(Uri contentUri) {
        Bitmap thumbnail = UtilsGeneral.getThumbnailVideo(getBaseContext(), contentUri);
        if (thumbnail != null) {
            videoImageBitmap = thumbnail;
            videoImageUri = null;

            binding.ivVideoImage.setImageBitmap(thumbnail);
        }
    }

    private final ActivityResultLauncher<String> getVideoImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(), result -> {
                        if (result != null) {
                            videoImageUri = result;
                            videoImageBitmap = null;

                            videoImageName = UtilsGeneral.getInstance().getFileName(result, getBaseContext());
                            binding.ivVideoImage.setImageURI(result);
                        }
                    }
            );

    private void checkFollowingProcess() {
        if (videoImageUri != null) {
            uploadVideoImage();
        } else if (videoImageBitmap != null) {
            uploadVideoImageBitmap();
        } else if (videoUri != null) {
            uploadVideoInForeground();
        } else {
            setVideo(getVideo());
        }
    }

    private Video getVideo() {
        return new Video(uuid, videoUrl, videoImageUrl, title, Timestamp.now());
    }
}
// User does not have permission to access this object.