package sree.myparty.volunteer;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import sree.myparty.DashBoard.SliderPagerAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.LatLng;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

public class VolunteerHomeFragment extends Fragment implements View.OnClickListener {


    LinearLayout db_op1, db_op2, db_op3, db_op5, db_op6;

    private FusedLocationProviderClient mFusedLocationClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    LatLng loginLocation = null;



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
        View v = inflater.inflate(R.layout.volunteer_home_fragment, null, false);


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

        db_op1 = (LinearLayout) v.findViewById(R.id.vol_db_op1);
        db_op2 = (LinearLayout) v.findViewById(R.id.vol_db_op2);
        db_op3 = (LinearLayout) v.findViewById(R.id.vol_db_op3);
     //   db_op4 = (LinearLayout) v.findViewById(R.id.vol_db_op4);
        db_op5 = (LinearLayout) v.findViewById(R.id.vol_db_op5);
        //  db_op6 = (ImageView)v.findViewById(R.id.vol_db_op6);*/

        db_op1.setOnClickListener(this);
        db_op2.setOnClickListener(this);
        db_op3.setOnClickListener(this);
      //  db_op4.setOnClickListener(this);
        db_op5.setOnClickListener(this);
        //  db_op6.setOnClickListener(this);*/

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                getLocation();
            }
        } else {
            getLocation();
        }
        return v;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vol_db_op1:
                ActivityLauncher.castewiseVoters(getActivity());
                break;
            case R.id.vol_db_op2:
                ActivityLauncher.influencePersons(getActivity());
                break;
            case R.id.vol_db_op3:
                ActivityLauncher.localIssues(getActivity());
                break;

            case R.id.vol_db_op5:
                ActivityLauncher.newsAcceptList(getActivity());
                break;

        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){

                    loginLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    String key = System.currentTimeMillis()+"";
                    MyApplication.getFirebaseDatabase()
                            .getReference(Constants.DB_PATH+"/VolunteerLocations")
                            .child(new SessionManager(getActivity()).getRegID()).child(key)
                            .setValue(loginLocation);
                }

            }
        });

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Alert")
                        .setMessage("You need us to use your location to work with this module")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
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
}
