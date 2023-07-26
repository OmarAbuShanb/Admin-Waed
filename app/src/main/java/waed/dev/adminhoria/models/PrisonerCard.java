package waed.dev.adminhoria.models;

import java.io.Serializable;

public class PrisonerCard implements Serializable {
    private String id;
    private String imageUrl;
    private String name;
    private String dateOfArrest;
    private String judgment;
    private String living;

    public PrisonerCard() {

    }

    public PrisonerCard(String id, String imageUrl, String name, String dateOfArrest, String judgment, String living) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.dateOfArrest = dateOfArrest;
        this.judgment = judgment;
        this.living = living;
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
}
