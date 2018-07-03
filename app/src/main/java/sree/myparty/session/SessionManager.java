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


    public SessionManager(Context gContex) {
        this.mContext=gContex;
        mSharedPref = mContext.getSharedPreferences(Constants.PREF_NAME,Context.MODE_PRIVATE);
        mEditor =mSharedPref.edit();
    }

    public void storeFirebaseKey(String key){
        mEditor.putString(Constants.FIREBASE_KEY,key);
        mEditor.commit();
    }

    public String getFirebaseKey(){
       return mSharedPref.getString(Constants.FIREBASE_KEY,"0");

       // return "eubrXBi1dEI:APA91bHnvVoGX8KdFZBMSkiGV7ULC8lBByPEwSSYLy31U3JH5SX3xenoN-jGAsr5zKlHrvXLPqHr18endH-e_HI8961UBfRSvlyLXgSofrLbzV8C3bI9xlO1YW6jH7_TsBg1M5ZI9Q0w7Bp_eSyiyOAdLaUciDaDGA";
    }
}
