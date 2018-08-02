package sree.myparty.constuecies;

import java.util.ArrayList;

/**
 * Created by srikanthk on 8/2/2018.
 */

public class PC {
    String pCName;

    public PC() {
    }

    public PC(String pCName, String district, ArrayList<String> assemblies) {
        this.pCName = pCName;
        this.district = district;
        this.assemblies = assemblies;
    }

    public String getpCName() {
        return pCName;
    }

    public void setpCName(String pCName) {
        this.pCName = pCName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    String district;
    ArrayList<String> assemblies;


    public ArrayList<String> getAssemblies() {
        return assemblies;
    }

    public void setAssemblies(ArrayList<String> assemblies) {
        this.assemblies = assemblies;
    }
}
