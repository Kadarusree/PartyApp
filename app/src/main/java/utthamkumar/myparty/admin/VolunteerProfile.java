package utthamkumar.myparty.admin;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.beans.VisitPojo;
import utthamkumar.myparty.beans.VolunteerLocationPojo;
import utthamkumar.myparty.pojos.LatLng;
import utthamkumar.myparty.pojos.VolunteerPojo;
import utthamkumar.myparty.session.SessionManager;
import utthamkumar.myparty.utils.Constants;

public class VolunteerProfile extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.id_profile_pic)
    CircleImageView mProfilePic;

    @BindView(R.id.id_QRCode)
    ImageView mQRcode;


    @BindView(R.id.id_tv_points)
    TextView mCount;

    @BindView(R.id.id_tv_refnumber)
    TextView mRefNumber;


    @BindView(R.id.profile_tv_name)
    TextView name;

    @BindView(R.id.profile_tv_voterID)
    TextView boothNumber;

    @BindView(R.id.profile_tv_mobileNumber)
    TextView mobileNumber;


    @BindView(R.id.profile_tv_position)
    TextView position;

    @BindView(R.id.profile_tv_committee)
    TextView committee;

    private GoogleMap mMap;

    ArrayList<VolunteerLocationPojo> mLocationPojos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.vol_map);
        mapFragment.getMapAsync(this);

        VolunteerPojo mVol = mGetVolunteer();
        name.setText("Name : " + mVol.getName());
        mRefNumber.setText(mVol.getRegID());
        mobileNumber.setText("Mobile Number : " + mVol.getMobileNumber());
        boothNumber.setText("Booth Number : " + mVol.getBoothnumber());
        committee.setText("Committee : " + mVol.getCommitte());
        position.setText("Position : " + mVol.getPosition());

        Glide.with(this).load(mVol.getQr_URl()).into(mQRcode);
        loadImage(this, mProfilePic, mVol.getProfilePic());
        getCount();

        getSupportActionBar().setTitle(mVol.getName());


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocation();



    }

    public VolunteerPojo mGetVolunteer() {
        VolunteerPojo mVoluneer = null;
        Intent i = getIntent();
        mVoluneer = (VolunteerPojo) i.getSerializableExtra("Volunteer");

        return mVoluneer;
    }

    public static void loadImage(final Activity context, ImageView imageView, String url) {
        if (context == null || context.isDestroyed()) return;

        //placeHolderUrl=R.drawable.ic_user;
        //errorImageUrl=R.drawable.ic_error;
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .placeholder(R.drawable.avatar) //this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.
                .error(R.drawable.avatar)//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.ALL) //using to load into cache then second time it will load fast.
                .animate(R.anim.fade_in) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                .fitCenter()//this method help to fit image into center of your ImageView
                .into(imageView); //pass imageView reference to appear the image.
    }

    public void getLocation() {
        mLocationPojos = new ArrayList<>();
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/VolunteerLocations")
                .child(mGetVolunteer().getRegID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {

                            for (DataSnapshot dpst : dataSnapshot.getChildren()) {
                                mLocationPojos.add(dpst.getValue(VolunteerLocationPojo.class));
                            }

                            if (mLocationPojos.size()>0){
                                placeMarker(mLocationPojos);
                            }

                        } else {
                            Constants.showToast("Last login location not available", VolunteerProfile.this);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Constants.showToast("Last login location not available", VolunteerProfile.this);
                    }
                });

    }

    private void placeMarker(ArrayList<VolunteerLocationPojo> mLocationPojos) {
        int lenghth = mLocationPojos.size();
        LatLng location = mLocationPojos.get(lenghth-1).getLocation();
        com.google.android.gms.maps.model.LatLng mLatLng = new com.google.android.gms.maps.model.LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mLatLng).title("Last Login Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 4000, null);
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }

    public void getCount() {

        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Voters")
                .orderByChild("added_by")
                .equalTo(mGetVolunteer().getName())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mCount.setText("Voters Added : " + dataSnapshot.getChildrenCount() + "");
                        ;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Constants.showToast("Last login location not available", VolunteerProfile.this);

                    }
                });

    }
}
