package utthamkumar.myparty.beans;

public class WishMePojo {

    int code;
    String path;
    boolean show;

    public WishMePojo(int code, String path, boolean show) {
        this.code = code;
        this.path = path;
        this.show = show;
    }  public WishMePojo() {

    }

    public int getCode() {
        return code;
    }

    public String getPath() {
        return path;
    }

    public boolean isShow() {
        return show;
    }
}
