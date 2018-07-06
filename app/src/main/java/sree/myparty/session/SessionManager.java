package sree.myparty.session;

import android.content.Context;
import android.content.SharedPreferences;

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
}
