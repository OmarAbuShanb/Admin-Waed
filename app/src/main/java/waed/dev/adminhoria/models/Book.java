package waed.dev.adminhoria.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

public class Book implements Serializable {
    private String uuid;
    private String imageUrl;
    private String name;
    private String author;
    private String pdfUrl;

    @ServerTimestamp
    private transient Timestamp timestamp;

    public Book() {
    }

    public Book(String uuid, String imageUrl, String name, String author, String pdfUrl, Timestamp timestamp) {
        this.uuid = uuid;
        this.imageUrl = imageUrl;
        this.name = name;
        this.author = author;
        this.pdfUrl = pdfUrl;
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

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
