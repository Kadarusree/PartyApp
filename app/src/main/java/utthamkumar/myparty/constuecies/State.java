package utthamkumar.myparty.constuecies;

import java.util.ArrayList;

/**
 * Created by srikanthk on 8/2/2018.
 */

public class State {
    String name;
    ArrayList<PC> pcs;

    public State() {
    }

    public State(String name, ArrayList<PC> pcs) {
        this.name = name;
        this.pcs = pcs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PC> getPcs() {
        return pcs;
    }

    public void setPcs(ArrayList<PC> pcs) {
        this.pcs = pcs;
    }
}
