package waed.dev.adminhoria.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

public class PrisonerCard implements Serializable {
    private String uuid;
    private String imageUrl;
    private String name;
    private String dateOfArrest;
    private String judgment;
    private String living;

    @ServerTimestamp
    private transient Timestamp timestamp;

    public PrisonerCard() {

    }

    public PrisonerCard(String uuid, String imageUrl, String name, String dateOfArrest, String judgment, String living, Timestamp timestamp) {
        this.uuid = uuid;
        this.imageUrl = imageUrl;
        this.name = name;
        this.dateOfArrest = dateOfArrest;
        this.judgment = judgment;
        this.living = living;
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

    public String getDateOfArrest() {
        return dateOfArrest;
    }

    public void setDateOfArrest(String dateOfArrest) {
        this.dateOfArrest = dateOfArrest;
    }

    public String getJudgment() {
        return judgment;
    }

    public void setJudgment(String judgment) {
        this.judgment = judgment;
    }

    public String getLiving() {
        return living;
    }

    public void setLiving(String living) {
        this.living = living;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
