package utthamkumar.myparty.constuecies;

import java.util.ArrayList;

/**
 * Created by srikanthk on 8/6/2018.
 */

public class Booths {
    public Booths(ArrayList<Booth> mBooths) {
        this.mBooths = mBooths;
    }

    public Booths() {
    }

    public ArrayList<Booth> getmBooths() {
        return mBooths;
    }

    public void setmBooths(ArrayList<Booth> mBooths) {
        this.mBooths = mBooths;
    }

    ArrayList<Booth> mBooths;


}
