package sree.myparty.DashBoard;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.beans.NewsPojo;
import sree.myparty.chat.UserListActicity;
import sree.myparty.chat.VideoCallActivity;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.pojos.VolunteerPojo;
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
    String news;

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
     Handler handler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, null, false);

        tv = (TextView) v.findViewById(R.id.mywidget);
        tv.setSelected(true);


        init( v);


        addBottomDots(0);

        handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);

        db_op1 = (ImageView) v.findViewById(R.id.db_op1);
       // db_op2 = (ImageView) v.findViewById(R.id.db_op2);
        db_op3 = (ImageView) v.findViewById(R.id.db_op3);
        db_op4 = (ImageView) v.findViewById(R.id.db_op4);
      //  db_op5 = (ImageView) v.findViewById(R.id.db_op5);
        db_op6 = (ImageView) v.findViewById(R.id.db_op6);
        db_op7 = (ImageView) v.findViewById(R.id.db_op7);
        db_op8 = (ImageView) v.findViewById(R.id.db_op8);
        db_op1.setOnClickListener(this);
      //  db_op2.setOnClickListener(this);
        db_op3.setOnClickListener(this);
        db_op4.setOnClickListener(this);
      //  db_op5.setOnClickListener(this);
        db_op6.setOnClickListener(this);
        db_op7.setOnClickListener(this);
        db_op8.setOnClickListener(this);

        mSessionManager = new SessionManager(getActivity());
        mVolunteerSessionManager = new VolunteerSessionManager(getActivity());
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/News").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                news = "";
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    NewsPojo mNewsItem = indi.getValue(NewsPojo.class);
                    news = news+"    "+mNewsItem.getTitle()+":"+mNewsItem.getDescription();
                }
                tv.setText(news);
                tv.setSelected(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
         /*   case R.id.db_op2:
              ActivityLauncher.launchWorkDoneListActivity(getActivity());
            //    ActivityLauncher.launchWorkDoneActivity(getActivity());

                break;*/
            case R.id.db_op3:
                startActivity(new Intent(getActivity(), UserListActicity.class));
                break;
            case R.id.db_op4:
                ActivityLauncher.launchMeetingsList(getActivity());
                break;
           /* case R.id.db_op5:
                if (mVolunteerSessionManager.hasActiveSession()) {
                    ActivityLauncher.volunteerDashboard(getActivity());
                } else {
                    ActivityLauncher.volunteerLoginScreen(getActivity());
                }
                break;*/
            case R.id.db_op6:
               isVolunteeer();
                break;
            case R.id.db_op7:
                ActivityLauncher.newsList(getActivity());


                break;
            case R.id.db_op8:
                ActivityLauncher.launchSurveyList(getActivity());

                break;
        }
    }



    private void init(View v) {

        /*setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().hide();*/
        vp_slider = (ViewPager) v.findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) v.findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();

//Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
// here i am adding few sample image links, you can add your own

        slider_image_list.add("https://www.congresslogo.com/images/quotes/english/general/congress-logo-download-52650-18545.jpg");
        slider_image_list.add("https://www.newslaundry.com/uploads/2017/11/Rahul-Gandhi-Article-Image.jpg");
        slider_image_list.add("https://www.congresslogo.com/images/quotes/english/general/congress-party-banner-52650-35879.jpg");
        slider_image_list.add("http://www.4to40.com/wp-content/uploads/2016/05/rajiv-gandhi-biography.jpg");
        slider_image_list.add("http://ste.india.com/sites/default/files/2015/12/30/446405-sonia.jpg");
        slider_image_list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvq8bXXMVohgHaaJydGdsgfbNpOPSJintEiKcYMr7hWcKImbSA");


        sliderPagerAdapter = new SliderPagerAdapter(getActivity(), slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            try {
                dots[i] = new TextView(getActivity());
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(Color.parseColor("#FFFFFF"));
                ll_dots.addView(dots[i]);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        if (dots.length > 0)
            if (dots[currentPage]!=null){
                dots[currentPage].setTextColor(Color.parseColor("#2ECC71"));
            }
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeMessages(0);
    }
    boolean yesVoluteer ;
    public boolean isVolunteeer(){
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/Volunteers").child(mSessionManager.getRegID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    VolunteerPojo mVolunteer = dataSnapshot.getValue(VolunteerPojo.class);
                    yesVoluteer = true;
                    if (mVolunteer.isAccepted()){
                        Constants.showToast("You are already a Volunteer,Please login",getActivity());
                        ActivityLauncher.volunteerLoginScreen(getActivity());
                    }
                    else {
                        Constants.showToast("Your application is pending for approval",getActivity());
                    }
                }
                else {
                   ActivityLauncher.volunteerRegistartionScreen(getActivity());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       return yesVoluteer;
    }
}
