package sree.myparty.utils;

import android.content.Context;
import android.content.SharedPreferences;

import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.pojos.VolunteerPojo;

/**
 * Created by srikanthk on 7/20/2018.
 */

public class VolunteerSessionManager {


    Context mContext;
    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;

    VolunteerPojo mPojo = new VolunteerPojo();


    private final String VOL_NAME="USERNAME";
    private final String MOBILE_NUMBER="MOBILE_NUMBER";
    private final String PROFILE_PIC="PROFILE_PIC";
    private final String QR_PIC="QR_PIC";
    private final String BOOTH_NUMBER="BOOTH_NUMBER";
    private final String REG_ID="REG_ID";
    private final String FCM_ID="FCM_ID";

    private final String PREF_NAME="VOLUNTEER";

    public VolunteerSessionManager(Context gContex) {
        this.mContext=gContex;
        mSharedPref = mContext.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        mEditor =mSharedPref.edit();

    }

    public void createUserSession(VolunteerPojo mPojo){
        mEditor.putString(VOL_NAME,mPojo.getName());
        mEditor.putString(MOBILE_NUMBER,mPojo.getMobileNumber());
        mEditor.putString(REG_ID,mPojo.getRegID());
        mEditor.putString(PROFILE_PIC,mPojo.getProfilePic());
        mEditor.putString(QR_PIC,mPojo.getQr_URl());
        mEditor.putString(FCM_ID,mPojo.getFcmID());
        mEditor.putString(BOOTH_NUMBER,mPojo.getBoothnumber());
        mEditor.commit();
    }

    public boolean hasActiveSession(){
        boolean yes=false;

        if (!mSharedPref.getString(REG_ID,"0").equalsIgnoreCase("0")){
            yes = true;
        }
        return yes;
    }

    public void logout(){
        mEditor.clear();
        mEditor.commit();
    }

    public String getVolName() {
        return mSharedPref.getString(VOL_NAME,"Default");
    }
}
