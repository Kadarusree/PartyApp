package sree.myparty.pojos;

public class UserDetailPojo {
    public UserDetailPojo() {

    }

   /* String firstName;
    String lastName;

    public UserDetailPojo(String firstName, String lastName, String voter_id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.voter_id = voter_id;
    }

    String voter_id;*/



    String voter_id;
    String mobile_number;
    String reg_id;
    String both_number;
    String user_name;
    String state_name;
    String pc_name;
    String ac_name;
    String fcm_id;
    String referal_code;

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getQr_url() {
        return qr_url;
    }

    public void setQr_url(String qr_url) {
        this.qr_url = qr_url;
    }

    String profile_url;
    String qr_url;
    int points;

    public UserDetailPojo(String voter_id, String mobile_number, String reg_id, String both_number, String user_name, String state_name, String pc_name, String ac_name, int points,String fcm_id,String profile_url,String qr_url,String referal_code) {
        this.voter_id = voter_id;
        this.mobile_number = mobile_number;
        this.reg_id = reg_id;
        this.both_number = both_number;
        this.user_name = user_name;
        this.state_name = state_name;
        this.pc_name = pc_name;
        this.ac_name = ac_name;
        this.points = points;
        this.fcm_id=fcm_id;
        this.profile_url=profile_url;
        this.qr_url=qr_url;
        this.referal_code=referal_code;
    }

    public String getReferal_code() {
        return referal_code;
    }

    public String getVoter_id() {
        return voter_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getReg_id() {
        return reg_id;
    }

    public String getBoth_number() {
        return both_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getState_name() {
        return state_name;
    }

    public String getPc_name() {
        return pc_name;
    }

    public String getAc_name() {
        return ac_name;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public int getPoints() {
        return points;
    }
}
