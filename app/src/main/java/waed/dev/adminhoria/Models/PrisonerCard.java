package waed.dev.adminhoria.Models;

public class PrisonerCard {
    private String id;
    private int imageUrl;
    private String name;
    private String dateOfArrest;
    private String judgment;
    private String living;

    public String getId() {
        return id;
    }

    public void setId(String prisonerId) {
        this.id = prisonerId;
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

    public PrisonerCard(String id, int imageUrl, String name, String dateOfArrest, String judgment, String living) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.dateOfArrest = dateOfArrest;
        this.judgment = judgment;
        this.living = living;
    }

    public PrisonerCard() {
    }
}
