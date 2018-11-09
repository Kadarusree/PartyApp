package utthamkumar.myparty.constuecies;

import utthamkumar.myparty.pojos.LatLng;

/**
 * Created by srikanthk on 8/6/2018.
 */

public class Booth {

    String boothNumber, name, location;

    public LatLng getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(LatLng mapLocation) {
        this.mapLocation = mapLocation;
    }

    LatLng mapLocation;

    public Booth(String boothNumber, String name, String location, LatLng mapLocation) {
        this.boothNumber = boothNumber;
        this.name = name;
        this.location = location;
        this.mapLocation = mapLocation;
    }

    public Booth() {
    }



    public String getBoothNumber() {
        return boothNumber;
    }

    public void setBoothNumber(String boothNumber) {
        this.boothNumber = boothNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
