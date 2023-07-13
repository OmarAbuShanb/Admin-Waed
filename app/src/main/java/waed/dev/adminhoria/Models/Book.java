package waed.dev.adminhoria.Models;

public class Book {
    private String id;
    private int imageUrl;
    private String name;
    private String author;
    private boolean isDownloaded;


    public Book() {
    }

    public Book(String id, int imageUrl, String name, String author) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.author = author;
    }

    public Book(String id, int imageUrl, String name, String author, boolean isDownloaded) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.author = author;
        this.isDownloaded = isDownloaded;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }
}
