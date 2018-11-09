package utthamkumar.myparty.pojos;

public class CasteWiseVoterBean {

    String name, voterID, caste, timestamp, createdby;
    int boothNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoterID() {
        return voterID;
    }

    public void setVoterID(String voterID) {
        this.voterID = voterID;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public int getBoothNum() {
        return boothNum;
    }

    public void setBoothNum(int boothNum) {
        this.boothNum = boothNum;
    }

    public CasteWiseVoterBean() {

    }

    public CasteWiseVoterBean(String name, String voterID, String caste, String timestamp, String createdby, int boothNum) {
        this.name = name;
        this.voterID = voterID;
        this.caste = caste;
        this.timestamp = timestamp;

        this.createdby = createdby;
        this.boothNum = boothNum;
    }
}
