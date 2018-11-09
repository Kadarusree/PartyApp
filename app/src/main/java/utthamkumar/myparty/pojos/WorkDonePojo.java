package utthamkumar.myparty.pojos;

/**
 * Created by srikanthk on 8/1/2018.
 */

public class WorkDonePojo {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key, workName, workArea, boothNumber, startDate, endDate,
            workImage, supervisor, contactNumber, addedBy, timeStamp;
    double moneySpent;
    int likes, dislikes;

    public WorkDonePojo() {
    }

    public WorkDonePojo(String key,String workName, String workArea, String boothNumber, String startDate, String endDate, String workImage, String supervisor, String contactNumber, String addedBy, String timeStamp, double moneySpent, int likes, int dislikes) {
        this.key = key;
        this.workName = workName;
        this.workArea = workArea;
        this.boothNumber = boothNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.workImage = workImage;
        this.supervisor = supervisor;
        this.contactNumber = contactNumber;
        this.addedBy = addedBy;
        this.timeStamp = timeStamp;
        this.moneySpent = moneySpent;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getBoothNumber() {
        return boothNumber;
    }

    public void setBoothNumber(String boothNumber) {
        this.boothNumber = boothNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getWorkImage() {
        return workImage;
    }

    public void setWorkImage(String workImage) {
        this.workImage = workImage;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
