package utthamkumar.myparty.pojos;

public class ReferalPojo {


    public String user_id;
    public ReferalPojo() {

    }
    public ReferalPojo(String user_id, String path) {
        this.user_id = user_id;
        this.path = path;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPath() {
        return path;
    }

    public   String path;
}
