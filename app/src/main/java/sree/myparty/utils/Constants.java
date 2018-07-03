package sree.myparty.utils;

import android.app.Activity;
import android.widget.Toast;

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

    public static String GKA_IMAGE_STORAGE_PATH="GKSurveyIMG";

    public static void showToast(String text, Activity mActivty){
        Toast.makeText(mActivty,text,Toast.LENGTH_LONG).show();
    }
}
