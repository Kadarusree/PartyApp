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
import sree.myparty.pojos.MeetingPojo;
import sree.myparty.volunteer.CasteWiseVoters;

/**
 * Created by srikanthk on 6/29/2018.
 */

public class Constants {

    public static  String FIREBASE_PUSH_API = "https://fcm.googleapis.com/fcm/send";
    public static  String FCM_SERVER_KEY = "key=AAAA4uqgDOE:APA91bEsiXQF6zCwtUMbEZBTt57KXmztAxHnVJt2t_cpXFRKlzSse1xSOnI6uG5GVUJuKAUNV9cC8oku8jGvbupNMwQ-N5q_KJ4mJU8fpLT_0OjOnSQs50uZ5bKnzuugh_-5vk1lz_a0clvCfVBuAmWeA9UEaFBDZg";


    //Session Constants:

    public static  String PREF_NAME = "myparty";
    public static  String FIREBASE_KEY = "firebasekey";
    public static  String USERNAME = "name";
    public static  String VOTERID = "voterID";
    public static  String CONSTIUNCY = "constiuency";


    //constants only used for data transmit from reg to otp
    public static  String VOTER_ID = "VOTER_ID";
    public static  String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static  String NAME = "NAME";
    public static  String VOLUNTEER = "Sree";
    public static  String REFRAL_TEMP_SESSION ="" ;
    //end

    public static String GKA_IMAGE_STORAGE_PATH="GKSurveyIMG";

    //
    public static String STATE = "";
    public static  String ASSEMBLY_CONST = "";
    public static  String PARLIMENT_CONST = "";

    public static String DB_PATH = STATE+"/"+PARLIMENT_CONST+"/"+ASSEMBLY_CONST;
    public static  String QR_URL = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=";

    public static MeetingPojo selected_meeting;
    public static String selected_booth_id;
    public static String selected_booth_name;
    public static String BOOTH_NUMBER;
    public static boolean isAdmin;

    public static void showToast(String text, Activity mActivty){
        Toast.makeText(mActivty,text,Toast.LENGTH_SHORT).show();
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
