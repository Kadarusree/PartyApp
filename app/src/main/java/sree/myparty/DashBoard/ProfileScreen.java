package sree.myparty.DashBoard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import sree.myparty.R;
import sree.myparty.session.SessionManager;

public class ProfileScreen extends AppCompatActivity {



    @BindView(R.id.id_profile_pic)
    ImageView mProfilePic;

    @BindView(R.id.id_QRCode)
    ImageView mQRcode;


    @BindView(R.id.id_tv_app_profilepic)
    TextView btnUpdateProfilePic;

    @BindView(R.id.id_tv_points)
    TextView mPoints;

    @BindView(R.id.id_tv_refnumber)
    TextView mRefNumber;

    SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        ButterKnife.bind(this);

        mSession = new SessionManager(this);

        Glide.with(this).load(mSession.getProfilePic()).into(mProfilePic);
        Glide.with(this).load(mSession.getQR()).into(mQRcode);

        mPoints.setText("Points : "+mSession.getPoints()+"");
        mRefNumber.setText( mSession.getRegID());


    }
}
