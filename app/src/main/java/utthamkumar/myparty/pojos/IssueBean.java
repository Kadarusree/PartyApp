package utthamkumar.myparty.pojos;

public class IssueBean {

    int boothnumber;
    long timestamp;
    String description, createdBy;


    public IssueBean(int boothnumber, long timestamp, String description, String createdBy) {
        this.boothnumber = boothnumber;
        this.timestamp = timestamp;
        this.description = description;
        this.createdBy = createdBy;
    }

    public IssueBean() {
    }

    public int getBoothnumber() {
        return boothnumber;
    }

    public void setBoothnumber(int boothnumber) {
        this.boothnumber = boothnumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
