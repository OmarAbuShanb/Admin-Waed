package waed.dev.adminhoria.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

public class Video implements Serializable {
    private String uuid;
    private String videoUrl;
    private String videoImageUrl;
    private String title;

    @ServerTimestamp
    private transient Timestamp timestamp;

    public Video() {
    }

    public Video(String uuid, String videoUrl, String videoImageUrl, String title, Timestamp timestamp) {
        this.uuid = uuid;
        this.videoUrl = videoUrl;
        this.videoImageUrl = videoImageUrl;
        this.title = title;
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
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
