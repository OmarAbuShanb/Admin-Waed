package waed.dev.adminhoria.models;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class AlbumImage {
    private String uuid;
    private String imageUrl;
    private Uri imageUri;

    @ServerTimestamp
    private transient Timestamp timestamp;

    public AlbumImage() {
    }

    // for base item in recycler
    public AlbumImage(String uuid) {
        this.uuid = uuid;
    }

    public AlbumImage(String uuid, String imageUrl, Timestamp timestamp) {
        this.uuid = uuid;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public AlbumImage(String uuid, Uri imageUri) {
        this.uuid = uuid;
        this.imageUri = imageUri;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
