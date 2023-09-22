package waed.dev.adminhoria.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Poster {
    private String uuid;
    private String imageUrl;

    @ServerTimestamp
    private transient Timestamp timestamp;

    public Poster() {}

    public Poster(String uuid, String imageUrl, Timestamp timestamp) {
        this.uuid = uuid;
        this.imageUrl = imageUrl;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
