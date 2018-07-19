package sree.myparty.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sree.myparty.DashBoard.BaseActvity;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;

public class UserListActicity extends AppCompatActivity {

    @BindView(R.id.recycler_view_user)
    RecyclerView  recycler_view_user;


    UserListAdapter userListAdapter;
    DatabaseReference databaseReference;
    ArrayList<UserDetailPojo> dataList;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist_layout);
        ButterKnife.bind(this);
        sessionManager=new SessionManager(getApplicationContext());
        userListAdapter=new UserListAdapter(UserListActicity.this,new ArrayList<UserDetailPojo>(),new SessionManager(getApplicationContext()));
        databaseReference= MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/Users");
        dataList=new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList=new ArrayList<>();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    UserDetailPojo userDetailPojo=dataSnapshot1.getValue(UserDetailPojo.class);
                  //  if(!sessionManager.getRegID().equalsIgnoreCase(userDetailPojo.getReg_id())) {
                        dataList.add(userDetailPojo);
                   // }




                }
                UserListAdapter adapter=new UserListAdapter(UserListActicity.this,dataList,new SessionManager(getApplicationContext()));
                recycler_view_user.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recycler_view_user.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }
}