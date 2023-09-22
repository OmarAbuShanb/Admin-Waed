package waed.dev.adminhoria;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import waed.dev.adminhoria.firebase.controller.FirebaseController;
import waed.dev.adminhoria.models.Video;

/**
 * Service to handle uploading files to Firebase Storage.
 */
public class UploadVideosService extends BaseTaskService {
    private static final String TAG = "MyUploadService";

    /**
     * Intent Actions
     **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    /**
     * Intent Extras
     **/
    public static final String EXTRA_VIDEO_URI = "extra_video_uri";
    public static final String EXTRA_VIDEO_NAME = "extra_video_name";
    public static final String EXTRA_VIDEO_MODEL = "extra_video_model";

    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    private FirebaseController firebaseController;

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseController = FirebaseController.getInstance();

        startForeground(
                PROGRESS_NOTIFICATION_ID,
                progressNotification(getString(R.string.app_name), 0)
        );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);

        if (ACTION_UPLOAD.equals(intent.getAction())) {
            Uri videoUri = Objects.requireNonNull(intent.getParcelableExtra(EXTRA_VIDEO_URI));
            String videoName = Objects.requireNonNull(intent.getSerializableExtra(EXTRA_VIDEO_NAME)).toString();
            Video videoModel = (Video) Objects.requireNonNull(intent.getSerializableExtra(EXTRA_VIDEO_MODEL));

            // Make sure we have permission to read the data
            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContentResolver().takePersistableUriPermission(
                        fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } */

            uploadFileUri(videoUri, videoModel, videoName);
        }

        return START_REDELIVER_INTENT;
    }

    private void uploadFileUri(Uri videoUri, Video video, String videoName) {
        taskStarted();

        StorageReference ref = FirebaseStorage.getInstance()
                .getReference("Videos")
                .child(video.getUuid())
                .child(videoName);

        ref.putFile(videoUri)
                .addOnProgressListener(taskSnapshot -> {
                    int progress = (int) (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());

                    startForeground(
                            PROGRESS_NOTIFICATION_ID,
                            progressNotification(video.getTitle(), progress)
                    );
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful() && task.getException() != null) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                })
                .addOnSuccessListener(uri -> {
                    video.setVideoUrl(uri.toString());
                    setVideo(video, videoUri);
                })
                .addOnFailureListener(e -> {
                    showUploadFinishedNotification(
                            null, videoUri, e.getMessage(), video.getTitle()
                    );
                    taskCompleted();
                });
    }

    private void setVideo(Video video, Uri videoUri) {
        firebaseController.setVideo(video, new FirebaseController.FirebaseCallback() {
            @Override
            public void onSuccess() {
                showUploadFinishedNotification(
                        video.getVideoUrl(),
                        videoUri,
                        getString(R.string.uploaded_successfully),
                        video.getTitle()
                );
                taskCompleted();
            }

            @Override
            public void onFailure(String errorMessage) {
                showUploadFinishedNotification(
                        null, videoUri, errorMessage, video.getTitle()
                );
                taskCompleted();
            }
        });
    }

    /**
     * Show a notification for a finished upload.
     */
    private void showUploadFinishedNotification(
            String downloadUrl,
            Uri fileUri,
            String caption,
            String title
    ) {
        // Hide the progress notification
        dismissProgressNotification();

        // Make Intent to MainActivity
        Intent intent = new Intent(this, MainActivity.class)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_VIDEO_URI, fileUri)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
//        String caption = success ? getString(R.string.upload_success) : getString(R.string.upload_failure);
        showFinishedNotification(caption, title, intent, success);
    }

    /**
     * Broadcast finished upload (success or failure).
     * return true if a running receiver received the broadcast.
     */
    private void broadcastUploadFinished(@Nullable String downloadUrl, @Nullable Uri fileUri) {
        boolean success = downloadUrl != null;
        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcastIntent = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_VIDEO_URI, fileUri);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }
}