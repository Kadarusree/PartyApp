package utthamkumar.myparty.beans;

import utthamkumar.myparty.pojos.LatLng;

public class VolunteerLocationPojo {

    LatLng location;
    String locationName;

    public VolunteerLocationPojo() {
    }

    public VolunteerLocationPojo(LatLng location, String locationName) {
        this.location = location;
        this.locationName = locationName;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
