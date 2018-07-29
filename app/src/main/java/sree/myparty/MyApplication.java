package sree.myparty;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by srikanthk on 6/26/2018.
 */

public class MyApplication extends Application {

    public static FirebaseDatabase mFirebaseDatabase;
    public static FirebaseStorage mFirebaseStorage;


    @Override
    public void onCreate() {
        super.onCreate();

        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

    }

    public static FirebaseDatabase getFirebaseDatabase(){
        return mFirebaseDatabase;
    }

    public static FirebaseStorage getFirebaseStorage(){
        return mFirebaseStorage;
    }

}
