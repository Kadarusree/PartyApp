package sree.myparty.pojos;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by srikanthk on 7/20/2018.
 */

public class VoterPojo {

    String voterID, voterName, voterFatherName, sex, mobileNumber, catageory, caste, boothNumber, address;

    LatLng location;

    int age;

    String added_by;

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public LatLng getAdded_location() {
        return added_location;
    }

    public void setAdded_location(LatLng added_location) {
        this.added_location = added_location;
    }

    LatLng added_location;



    public VoterPojo(String voterID, String voterName, String voterFatherName, String sex,int age, String mobileNumber,String address, String catageory, String caste, String boothNumber,  LatLng location, String added_by, LatLng added_location) {
        this.voterID = voterID;
        this.voterName = voterName;
        this.voterFatherName = voterFatherName;
        this.sex = sex;
        this.age = age;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.catageory = catageory;
        this.caste = caste;
        this.boothNumber = boothNumber;
        this.location = location;
        this.added_by = added_by;
        this.added_location = added_location;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getVoterFatherName() {
        return voterFatherName;
    }

    public void setVoterFatherName(String voterFatherName) {
        this.voterFatherName = voterFatherName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCatageory() {
        return catageory;
    }

    public void setCatageory(String catageory) {
        this.catageory = catageory;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getBoothNumber() {
        return boothNumber;
    }

    public void setBoothNumber(String boothNumber) {
        this.boothNumber = boothNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
