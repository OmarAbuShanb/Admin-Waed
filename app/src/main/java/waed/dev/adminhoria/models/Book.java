package waed.dev.adminhoria.models;

import android.os.Parcelable;

import java.io.Serializable;

public class Book implements Serializable {
    private String id;
    private String imageUrl;
    private String name;
    private String author;
    private String pdfUrl;


    public Book() {
    }

    public Book(String id, String imageUrl, String name, String author, String pdfUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.author = author;
        this.pdfUrl = pdfUrl;
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
}
