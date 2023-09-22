package waed.dev.adminhoria.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

public class Statistic implements Serializable {
    private String uuid;
    private String title;
    private String iconUrl;
    private int number;
    private String pdfUrl;

    // transient => Parcelable encountered IOException writing serializable object
    @ServerTimestamp
    private transient Timestamp timestamp;

    public Statistic() {

    }

    public Statistic(String uuid, String title, String iconUrl, int number, String pdfUrl, Timestamp timestamp) {
        this.uuid = uuid;
        this.title = title;
        this.iconUrl = iconUrl;
        this.number = number;
        this.pdfUrl = pdfUrl;
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
