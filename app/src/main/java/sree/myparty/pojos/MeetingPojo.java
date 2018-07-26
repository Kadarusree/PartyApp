package sree.myparty.pojos;


/**
 * Created by srikanthk on 7/26/2018.
 */

public class MeetingPojo {

    String meetingName, meetingPurpose, meetingDateTime, meetingVenue,  scheduledBy;
    LatLng meetingLocation;
    boolean isForAll;

    public MeetingPojo(String meetingName, String meetingPurpose, String meetingDateTime, String meetingVenue, boolean isForAll, String scheduledBy, LatLng meetingLocation) {
        this.meetingName = meetingName;
        this.meetingPurpose = meetingPurpose;
        this.meetingDateTime = meetingDateTime;
        this.meetingVenue = meetingVenue;
        this.isForAll = isForAll;
        this.scheduledBy = scheduledBy;
        this.meetingLocation = meetingLocation;
    }


    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingPurpose() {
        return meetingPurpose;
    }

    public void setMeetingPurpose(String meetingPurpose) {
        this.meetingPurpose = meetingPurpose;
    }

    public String getMeetingDateTime() {
        return meetingDateTime;
    }

    public void setMeetingDateTime(String meetingDateTime) {
        this.meetingDateTime = meetingDateTime;
    }

    public String getMeetingVenue() {
        return meetingVenue;
    }

    public void setMeetingVenue(String meetingVenue) {
        this.meetingVenue = meetingVenue;
    }

    public boolean getIsForAll() {
        return isForAll;
    }

    public void setIsForAll(boolean isForAll) {
        this.isForAll = isForAll;
    }

    public String getScheduledBy() {
        return scheduledBy;
    }

    public void setScheduledBy(String scheduledBy) {
        this.scheduledBy = scheduledBy;
    }

    public LatLng getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(LatLng meetingLocation) {
        this.meetingLocation = meetingLocation;
    }
}
