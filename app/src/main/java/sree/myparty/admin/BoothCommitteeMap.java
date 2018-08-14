package sree.myparty.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.constuecies.Booth;
import sree.myparty.database.DatabaseHelper;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.pojos.VoterPojo;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

public class BoothCommitteeMap extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ProgressDialog mProgressDialog;
    private List<Booth> mBoothsList;

    ArrayList<VolunteerPojo> mVotersList;
    private DatabaseHelper db;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth_committee_map);
        mProgressDialog = Constants.showDialog(this);
        mBoothsList = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        db = new DatabaseHelper(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                getLocation();
            }
        } else {
            getLocation();
        }


        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Booths/mBooths").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                mBoothsList.clear();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    Booth mBooth = indi.getValue(Booth.class);
                    mBoothsList.add(mBooth);
                }

                loadAllVolunteers();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Booth mBooth = (Booth) marker.getTag();
                if (mBooth != null) {
                   /* Toast.makeText(getApplicationContext(), mBooth.getBoothNumber(), Toast.LENGTH_LONG).show();
                    Constants.selected_booth_id = marker.getTitle();
                    Constants.selected_booth_name = mBooth.getName();

                    ActivityLauncher.launchBoothWiseVotesAnalysyis(BoothCommitteeMap.this);
         */       } else {
                    Toast.makeText(getApplicationContext(), "Booth Not Available", Toast.LENGTH_LONG).show();

                }
                return false;
            }
        });
    }

    private void placeMarkers(List<Booth> mBoothsList) {
        for (int i = 0; i < mBoothsList.size(); i++) {

            Booth mBooth = mBoothsList.get(i);

            if (mBooth.getMapLocation() != null) {
                com.google.android.gms.maps.model.LatLng markerLocation = new com.google.android.gms.maps.model.LatLng(mBooth.getMapLocation().getLatitude(), mBooth.getMapLocation().getLongitude());

                MarkerOptions mMarker = new MarkerOptions();
                mMarker.position(markerLocation);
                mMarker.title(mBooth.getBoothNumber());
                int percentage = getCount(mBooth.getBoothNumber());

               if (percentage > 0 && percentage < 10) {
                    mMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                } else if (percentage > 10) {
                    mMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
               else {
                   mMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
               }

                mMap.addMarker(mMarker).setTag(mBooth);
            }

        }
    }

    private int  getCount(String boothNumber) {

        return db.getBoothwiseVolunteersCount(boothNumber);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("You need us to use your location to work with this module")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BoothCommitteeMap.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude()), 12));
                            mMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
                        }
                    }
                });
    }

    public void getBooths(){

    }


    public void loadAllVolunteers() {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Volunteers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mVotersList = new ArrayList<>();

                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    VolunteerPojo mPojo = indi.getValue(VolunteerPojo.class);
                    mVotersList.add(mPojo);
                }
                db.insertVolunteers(mVotersList);



                placeMarkers(mBoothsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        db.delete();
    }
}
