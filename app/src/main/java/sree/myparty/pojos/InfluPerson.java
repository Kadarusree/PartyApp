package sree.myparty.pojos;

/**
 * Created by srikanthk on 7/5/2018.
 */

public class InfluPerson {

    String name;
    String mobileNumber;
    String timestamp;

    public InfluPerson(String name, String mobileNumber, String timestamp, String addedBy, int boothNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.timestamp = timestamp;
        this.addedBy = addedBy;
        this.boothNumber = boothNumber;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    String addedBy;
    int boothNumber;

    public InfluPerson() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getBoothNumber() {
        return boothNumber;
    }

    public void setBoothNumber(int boothNumber) {
        this.boothNumber = boothNumber;
    }
}
