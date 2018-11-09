package utthamkumar.myparty.pojos;

/**
 * Created by srikanthk on 7/5/2018.
 */

public class InfluPerson {

    String name;
    String mobileNumber;
    String timestamp;
    String profile_pic;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    String location_name;
    LatLng location;

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    String reg_id;

    public InfluPerson(String reg_id,String name, String mobileNumber, String timestamp, String profile_pic, String location_name, LatLng location, String addedBy, int boothNumber) {


        this.reg_id = reg_id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.timestamp = timestamp;
        this.profile_pic = profile_pic;
        this.location_name = location_name;
        this.location = location;
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
