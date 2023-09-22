package waed.dev.adminhoria;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * Base class for Services that keep track of the number of active jobs and self-stop when the
 * count is zero.
 */
public abstract class BaseTaskService extends Service {
    private static final String CHANNEL_ID_DEFAULT = "default";

    static final int PROGRESS_NOTIFICATION_ID = 1;
    static final int FINISHED_NOTIFICATION_ID = 2;

    private static final String TAG = "MyBaseTaskService";
    private int mNumTasks = 0;

    public void taskStarted() {
        changeNumberOfTasks(1);
    }

    public void taskCompleted() {
        changeNumberOfTasks(-1);
    }

    public int getNumTasks() {
        return mNumTasks;
    }

    private synchronized void changeNumberOfTasks(int delta) {
        Log.d(TAG, "changeNumberOfTasks:" + mNumTasks + ":" + delta);
        mNumTasks += delta;

        // If there are no tasks left, stop the service
        if (mNumTasks <= 0) {
            Log.d(TAG, "stopping");
            stopSelf();
        }
    }

    private void createDefaultChannel() {
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_DEFAULT,
                    "Default",
                    NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
        }
    }

    /**
     * Show notification with a progress bar.
     */
    protected Notification progressNotification(String title, int percentComplete) {
        createDefaultChannel();

        boolean indeterminate = getNumTasks() > 1;

        String caption;
        if (getNumTasks() > 1) {
            title = getString(R.string.app_name);

            caption = getString(R.string.uploading)
                    .concat(" ")
                    .concat(String.valueOf(getNumTasks()))
                    .concat(" ")
                    .concat(getString(R.string.video));
        } else {
            caption = getString(R.string.progress_uploading);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_DEFAULT)
                .setSmallIcon(R.drawable.ic_file_upload_white_24dp)
                .setContentTitle(title)
                .setContentText(caption)
                .setProgress(100, percentComplete, indeterminate)
                .setOngoing(true)
                .setAutoCancel(false);

        return builder.build();
    }

    /**
     * Show notification that the activity finished.
     */
    protected void showFinishedNotification(String caption, String title, Intent intent, boolean success) {
        // Make PendingIntent for notification
        int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? PendingIntent.FLAG_IMMUTABLE
                : PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent
                = PendingIntent.getActivity(this, 0 /* requestCode */, intent, flag);

        int icon = success ? R.drawable.ic_check_white_24 : R.drawable.ic_error_white_24dp;

        createDefaultChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_DEFAULT)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(caption)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(FINISHED_NOTIFICATION_ID, builder.build());
    }

    /**
     * Dismiss the progress notification.
     */
    protected void dismissProgressNotification() {
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(PROGRESS_NOTIFICATION_ID);
    }
}