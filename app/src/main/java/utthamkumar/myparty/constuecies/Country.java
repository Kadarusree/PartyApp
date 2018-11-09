package utthamkumar.myparty.constuecies;

import java.util.ArrayList;

/**
 * Created by srikanthk on 8/2/2018.
 */

public class Country {

    public Country() {
    }

    public Country(ArrayList<State> states) {
        this.states = states;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    ArrayList<State> states;
}
