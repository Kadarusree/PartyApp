package sree.myparty.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.util.ArrayList;

import sree.myparty.R;
import sree.myparty.volunteer.CasteWiseVoters;

/**
 * Created by srikanthk on 6/29/2018.
 */

public class Constants {

    public static final String FIREBASE_PUSH_API = "https://fcm.googleapis.com/fcm/send";
    public static final String FCM_SERVER_KEY = "key=AAAA4uqgDOE:APA91bEsiXQF6zCwtUMbEZBTt57KXmztAxHnVJt2t_cpXFRKlzSse1xSOnI6uG5GVUJuKAUNV9cC8oku8jGvbupNMwQ-N5q_KJ4mJU8fpLT_0OjOnSQs50uZ5bKnzuugh_-5vk1lz_a0clvCfVBuAmWeA9UEaFBDZg";


    //Session Constants:

    public static final String PREF_NAME = "myparty";
    public static final String FIREBASE_KEY = "firebasekey";
    public static final String USERNAME = "name";
    public static final String VOTERID = "voterID";
    public static final String CONSTIUNCY = "constiuency";


    //constants only used for data transmit from reg to otp
    public static final String VOTER_ID = "VOTER_ID";
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static final String NAME = "NAME";
    public static final String VOLUNTEER = "Sree";
    //end

    public static String GKA_IMAGE_STORAGE_PATH="GKSurveyIMG";

    //
    public static String STATE = "Telangana";
    public static final String ASSEMBLY_CONST = "Sirpur";
    public static final String PARLIMENT_CONST = "Adilabad";

    public static String DB_PATH = STATE+"/"+PARLIMENT_CONST+"/"+ASSEMBLY_CONST;
    public static final String QR_URL = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=";



    public static void showToast(String text, Activity mActivty){
        Toast.makeText(mActivty,text,Toast.LENGTH_LONG).show();
    }


    static ProgressDialog mDialog;

    public static ProgressDialog showDialog(Activity act) {


            mDialog =  new ProgressDialog(act);
            mDialog.setMessage("Please Wait");
            mDialog.setCancelable(false);

        return mDialog;
    }
    public static ArrayList<String> fcm_ids;
}
