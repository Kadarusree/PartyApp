package sree.myparty.pojos;

import java.io.Serializable;

/**
 * Created by srikanthk on 7/12/2018.
 */

public class VolunteerPojo implements Serializable {

    String name;

    public VolunteerPojo() {
    }

    String boothnumber;
    String regID;
    String password;
    String fcmID;
    String profilePic;
    String qr_URl;
    String mobileNumber;
    boolean isAccepted;

    public VolunteerPojo(String name, String boothnumber, String regID, String password, String fcmID, String profilePic, String qr_URl, String mobileNumber, boolean isAccepted) {
        this.name = name;
        this.boothnumber = boothnumber;
        this.regID = regID;
        this.password = password;
        this.fcmID = fcmID;
        this.profilePic = profilePic;
        this.qr_URl = qr_URl;
        this.mobileNumber = mobileNumber;
        this.isAccepted = isAccepted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoothnumber() {
        return boothnumber;
    }

    public void setBoothnumber(String boothnumber) {
        this.boothnumber = boothnumber;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcmID() {
        return fcmID;
    }

    public void setFcmID(String fcmID) {
        this.fcmID = fcmID;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getQr_URl() {
        return qr_URl;
    }

    public void setQr_URl(String qr_URl) {
        this.qr_URl = qr_URl;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
