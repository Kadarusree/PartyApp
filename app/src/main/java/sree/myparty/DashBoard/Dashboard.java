package sree.myparty.DashBoard;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.ReferalPojo;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 6/26/2018.
 */

public class Dashboard extends BaseActvity {
    DatabaseReference reference;
    FirebaseAuth auth;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);
        reference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Users/" + auth.getUid());
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("fcm_id", sessionManager.getFirebaseKey());
        reference.updateChildren(taskMap);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    Log.d("shri", dataSnapshot.getChildrenCount() + "");
                    UserDetailPojo pojo = dataSnapshot.getValue(UserDetailPojo.class);

                    if (pojo != null) {
                        SessionManager mSessionManager = new SessionManager(Dashboard.this);
                        mSessionManager.createUserSession(pojo);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}


