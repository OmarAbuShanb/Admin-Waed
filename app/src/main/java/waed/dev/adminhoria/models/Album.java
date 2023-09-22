package waed.dev.adminhoria.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

public class Album implements Serializable {
    private String uuid;
    private String imageUrl;
    private String title;

    @ServerTimestamp
    private transient Timestamp timestamp;

    public Album() {
    }

    public Album(String uuid, String imageUrl, String title, Timestamp timestamp) {
        this.uuid = uuid;
        this.imageUrl = imageUrl;
        this.title = title;
        this.timestamp = timestamp;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
