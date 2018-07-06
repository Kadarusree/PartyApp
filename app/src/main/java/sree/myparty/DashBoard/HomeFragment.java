package sree.myparty.DashBoard;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 6/28/2018.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

     ImageView db_op1,db_op2,db_op3,db_op4,db_op5,db_op6;
     FirebaseAuth auth;
     DatabaseReference reference;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.home_fragment,null,false);
        auth=FirebaseAuth.getInstance();

        reference=  MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH +"/Users/"+auth.getUid());
        db_op1 = (ImageView)v.findViewById(R.id.db_op1);
        db_op2 = (ImageView)v.findViewById(R.id.db_op2);
        db_op3 = (ImageView)v.findViewById(R.id.db_op3);
        db_op4 = (ImageView)v.findViewById(R.id.db_op4);
        db_op5 = (ImageView)v.findViewById(R.id.db_op5);
        db_op6 = (ImageView)v.findViewById(R.id.db_op6);
        db_op1.setOnClickListener(this);
        db_op2.setOnClickListener(this);
        db_op3.setOnClickListener(this);
        db_op4.setOnClickListener(this);
        db_op5.setOnClickListener(this);
        db_op6.setOnClickListener(this);



       /* db_op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.db_op1:
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot!=null)
                        {

                            Log.d("shri",dataSnapshot.getChildrenCount()+"");
                              UserDetailPojo pojo=dataSnapshot.getValue(UserDetailPojo.class);
                                    if(pojo!=null)
                                    {
                                        Dialog d = new Dialog(getActivity());
                                        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        d.setContentView(R.layout.id_card);
                                        TextView tv_username=d.findViewById(R.id.tv_username);
                                        TextView tv_user_voterid= d.findViewById(R.id.tv_user_voterid);
                                        TextView tv_user_RegId= d.findViewById(R.id.tv_user_RegId);
                                        tv_username.setText(pojo.getUser_name());
                                        tv_user_voterid.setText(pojo.getVoter_id());
                                        tv_user_RegId.setText(pojo.getReg_id());

                                        d.show();

                                    }



                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case R.id.db_op2:
                ActivityLauncher.volunteerRegistartionScreen(getActivity());
                break;
            case R.id.db_op3:
                break;
            case R.id.db_op4:
                break;
            case R.id.db_op5:
                ActivityLauncher.volunteerLoginScreen(getActivity());

                break;
            case R.id.db_op6:
                ActivityLauncher.adminLogin(getActivity());

                break;
        }
    }
}
