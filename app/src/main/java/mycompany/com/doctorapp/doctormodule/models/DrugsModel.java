package mycompany.com.doctorapp.doctormodule.models;

/**
 * Created by sciens1 on 8/12/2016.
 */
public class DrugsModel {
    private String drugName;
    private String description;
    private String imageUrl;

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
