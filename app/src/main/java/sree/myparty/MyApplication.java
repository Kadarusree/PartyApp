package sree.myparty;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by srikanthk on 6/26/2018.
 */

public class MyApplication extends Application {

    public static FirebaseDatabase mFirebaseDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        mFirebaseDatabase = mFirebaseDatabase.getInstance();
    }

    public static FirebaseDatabase getFirebaseDatabase(){
        return mFirebaseDatabase;
    }

}
