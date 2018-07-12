package sree.myparty.session;

import android.content.Context;
import android.content.SharedPreferences;

import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 7/2/2018.
 */

public class SessionManager {


    Context mContext;
    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;
    private final String STATE="STATE_NAME";
    private final String PC_NAME="PC_NAME";
    private final String AC_NAME="AC_NAME";


    private final String USERNAME="USERNAME";
    private final String VOTER_ID="VOTER_ID";
    private final String MOBILE_NUMBER="MOBILE_NUMBER";
    private final String PROFILE_PIC="PROFILE_PIC";
    private final String QR_PIC="QR_PIC";
    private final String BOOTH_NUMBER="BOOTH_NUMBER";
    private final String POINTS="POINTS";
    private final String REG_ID="REG_ID";
    private final String FCM_ID="FCM_ID";

    public SessionManager(Context gContex) {
        this.mContext=gContex;
        mSharedPref = mContext.getSharedPreferences(Constants.PREF_NAME,Context.MODE_PRIVATE);
        mEditor =mSharedPref.edit();
    }

    public void storeFirebaseKey(String key){
        mEditor.putString(Constants.FIREBASE_KEY,key);
        mEditor.commit();
    }

    public void storeConstiuencyInfo(String state,String pc,String ac)
    {
        mEditor.putString(STATE,state);
        mEditor.putString(PC_NAME,pc);
        mEditor.putString(AC_NAME,ac);
        mEditor.commit();
    }

    public String getState()
    {
        return mSharedPref.getString(STATE,"");

    }
    public String getPC_NAME()
    {
        return mSharedPref.getString(PC_NAME,"");

    }public String getAC_NAME()
    {
        return mSharedPref.getString(AC_NAME,"");

    }


    public String getFirebaseKey(){
       return mSharedPref.getString(Constants.FIREBASE_KEY,"0");

       // return "eubrXBi1dEI:APA91bHnvVoGX8KdFZBMSkiGV7ULC8lBByPEwSSYLy31U3JH5SX3xenoN-jGAsr5zKlHrvXLPqHr18endH-e_HI8961UBfRSvlyLXgSofrLbzV8C3bI9xlO1YW6jH7_TsBg1M5ZI9Q0w7Bp_eSyiyOAdLaUciDaDGA";
    }


    public void createUserSession(UserDetailPojo mPojo){
        mEditor.putString(USERNAME,mPojo.getUser_name());
        mEditor.putString(VOTER_ID,mPojo.getVoter_id());
        mEditor.putString(MOBILE_NUMBER,mPojo.getMobile_number());
        mEditor.putString(REG_ID,mPojo.getReg_id());
        mEditor.putString(PROFILE_PIC,mPojo.getProfile_url());
        mEditor.putString(QR_PIC,mPojo.getQr_url());
        mEditor.putString(FCM_ID,mPojo.getFcm_id());
        mEditor.putString(AC_NAME,mPojo.getAc_name());
        mEditor.putString(PC_NAME,mPojo.getPc_name());
        mEditor.putString(STATE,mPojo.getState_name());
        mEditor.putInt(POINTS,mPojo.getPoints());
        mEditor.commit();

    }

    public String getQR(){
        return mSharedPref.getString(QR_PIC,"");
    }
    public String getMobile(){
        return mSharedPref.getString(MOBILE_NUMBER,"");
    }
    public String getName(){
        return mSharedPref.getString(USERNAME,"");
    }
    public String getVoterID(){
        return mSharedPref.getString(VOTER_ID,"");
    }
    public String getProfilePic(){
        return mSharedPref.getString(PROFILE_PIC,"");
    }
    public String getRegID(){
        return mSharedPref.getString(REG_ID,"");
    }

}
