package sree.myparty.DashBoard;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.chat.UserListActicity;
import sree.myparty.chat.VideoCallActivity;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.survey.SurveyList;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;
import sree.myparty.utils.VolunteerSessionManager;

/**
 * Created by srikanthk on 6/28/2018.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageView db_op1, db_op2, db_op3, db_op4, db_op5, db_op6,db_op7, db_op8;
    SessionManager mSessionManager;


    VolunteerSessionManager mVolunteerSessionManager;

    TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, null, false);

        tv = (TextView) v.findViewById(R.id.mywidget);
        tv.setSelected(true);


        db_op1 = (ImageView) v.findViewById(R.id.db_op1);
        db_op2 = (ImageView) v.findViewById(R.id.db_op2);
        db_op3 = (ImageView) v.findViewById(R.id.db_op3);
        db_op4 = (ImageView) v.findViewById(R.id.db_op4);
        db_op5 = (ImageView) v.findViewById(R.id.db_op5);
        db_op6 = (ImageView) v.findViewById(R.id.db_op6);
        db_op7 = (ImageView) v.findViewById(R.id.db_op7);
        db_op8 = (ImageView) v.findViewById(R.id.db_op8);
        db_op1.setOnClickListener(this);
        db_op2.setOnClickListener(this);
        db_op3.setOnClickListener(this);
        db_op4.setOnClickListener(this);
        db_op5.setOnClickListener(this);
        db_op6.setOnClickListener(this);
        db_op7.setOnClickListener(this);
        db_op8.setOnClickListener(this);

        mSessionManager = new SessionManager(getActivity());
        mVolunteerSessionManager = new VolunteerSessionManager(getActivity());


        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.db_op1:
                /*Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.id_card);
                TextView tv_username=d.findViewById(R.id.tv_username);
                TextView tv_user_voterid= d.findViewById(R.id.tv_user_voterid);
                TextView tv_user_RegId= d.findViewById(R.id.tv_user_RegId);
                tv_username.setText(mSessionManager.getName());
                tv_user_voterid.setText(mSessionManager.getVoterID());
                tv_user_RegId.setText(mSessionManager.getRegID());

                ImageView profilePIC=d.findViewById(R.id.profile_pic);
                ImageView Qrpic= d.findViewById(R.id.qr);

                Glide.with(getActivity()).load(mSessionManager.getQR()).into(Qrpic);
                Glide.with(getActivity()).load(mSessionManager.getProfilePic()).into(profilePIC);

                d.show();
*/

                ActivityLauncher.profileScreen(getActivity());

                break;
            case R.id.db_op2:
                ActivityLauncher.launchWorkDoneListActivity(getActivity());
                break;
            case R.id.db_op3:
                startActivity(new Intent(getActivity(), UserListActicity.class));
                break;
            case R.id.db_op4:
                ActivityLauncher.launchMeetingsList(getActivity());
                break;
            case R.id.db_op5:
                if (mVolunteerSessionManager.hasActiveSession()) {
                    ActivityLauncher.volunteerDashboard(getActivity());
                } else {
                    ActivityLauncher.volunteerLoginScreen(getActivity());
                }
                break;
            case R.id.db_op6:
                ActivityLauncher.adminLogin(getActivity());
                break;
            case R.id.db_op7:
                ActivityLauncher.newsList(getActivity());


                break;
            case R.id.db_op8:
                ActivityLauncher.launchSurveyList(getActivity());


                break;
        }
    }
}
