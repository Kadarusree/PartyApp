package sree.myparty.admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.constuecies.Booth;
import sree.myparty.database.DatabaseHelper;
import sree.myparty.pojos.VoterPojo;
import sree.myparty.utils.Constants;

public class PresentTrend extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ProgressDialog mProgressDialog;
    private List<Booth> mBoothsList;

    ArrayList<VoterPojo> mVotersList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_trend);
        mProgressDialog = Constants.showDialog(this);
        mBoothsList = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new DatabaseHelper(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Booths/mBooths").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();

                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    Booth mBooth = indi.getValue(Booth.class);
                    mBoothsList.add(mBooth);
                }
                loadAllVoters();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void placemarkers(List<Booth> mBoothsList, GoogleMap googleMap) {
        for (int i = 0; i < mBoothsList.size(); i++) {

            Booth mBooth = mBoothsList.get(i);

            if (mBooth.getMapLocation() != null) {
                com.google.android.gms.maps.model.LatLng markerLocation = new com.google.android.gms.maps.model.LatLng(mBooth.getMapLocation().getLatitude(), mBooth.getMapLocation().getLongitude());
                MarkerOptions mMarker = new MarkerOptions();
                mMarker.position(markerLocation);
                mMarker.title(mBooth.getBoothNumber() + " " + mBooth.getName());

                if (getCount(mBooth.getBoothNumber())>0){
                    mMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                else {
                    mMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                }
                googleMap.addMarker(mMarker);

            }

        }
    }



    public void loadAllVoters(){
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Voters").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mVotersList = new ArrayList<>();

                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    VoterPojo mPojo = indi.getValue(VoterPojo.class);
                    mVotersList.add(mPojo);
                }
                db.insertVoters(mVotersList);

                placemarkers(mBoothsList,mMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int getCount(String id){
        return db.getBoothwiseVoters(id).size();
    }
}
