package waed.dev.adminhoria.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

public class News implements Serializable {
    private String uuid;
    private String imageUrl;
    private String title;
    private String details;

    @ServerTimestamp
    private transient Timestamp timestamp;

    public News(String uuid, String imageUrl, String title, String details, Timestamp timestamp) {
        this.uuid = uuid;
        this.imageUrl = imageUrl;
        this.title = title;
        this.details = details;
        this.timestamp = timestamp;
    }

    public News() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
