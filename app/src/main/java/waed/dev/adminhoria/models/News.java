package waed.dev.adminhoria.models;

import java.io.Serializable;

public class News implements Serializable {
    private String id;
    private String imageUrl;
    private String title;
    private String details;

    public News(String id, String imageUrl, String title, String details) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.details = details;
    }

    public News() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
