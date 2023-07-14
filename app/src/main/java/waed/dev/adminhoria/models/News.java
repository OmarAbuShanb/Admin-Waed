package waed.dev.adminhoria.models;

public class News {
    private String id;
    private int imageUrl;
    private String title;
    private String details;

    public News(String id, int imageUrl, String title, String details) {
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

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
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
