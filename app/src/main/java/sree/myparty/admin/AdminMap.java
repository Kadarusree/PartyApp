package sree.myparty.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.constuecies.Booth;
import sree.myparty.pojos.VoterPojo;
import sree.myparty.utils.Constants;

public class AdminMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    DatabaseReference mReference;

    ArrayList<VoterPojo> mVotersList;


    @BindView(R.id.rb_booth)
    RadioButton booth;

    @BindView(R.id.rb_const)
    RadioButton constitution;


    @BindView(R.id.boothLayout)
    LinearLayout boothLayout;

    @BindView(R.id.edt_booth)
    Spinner boothNumber;

    ProgressDialog mDialog;
/*
    @BindView(R.id.top_bar_activity_toolbar)
    Toolbar toolbar;*/


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient mFusedLocationClient;


    ProgressDialog mProgressDialog;


    ArrayList<Booth> mBoothsList;
    ArrayList<String> boothNames;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_map);
        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
        mDialog = Constants.showDialog(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mReference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Voters");
        constitution.setChecked(true);
        boothLayout.setVisibility(View.GONE);
        constitution.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    booth.setChecked(false);
                    boothLayout.setVisibility(View.GONE);
                    onMapReady(mMap);
                }
            }
        });

        booth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    constitution.setChecked(false);
                    boothLayout.setVisibility(View.VISIBLE);
                    boothNames = new ArrayList<>();
                    loadBooths();

                    boothNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mMap.clear();

                            if (mBoothsList.size() > 0) {
                                Booth mBooth = mBoothsList.get(position);
                                searchByBooth(mBoothsList.get(position).getBoothNumber());

                                if (mBooth.getMapLocation() != null) {
                                    MarkerOptions mMarker = new MarkerOptions();
                                    mMarker.position(new LatLng(mBooth.getMapLocation().getLatitude(), mBooth.getMapLocation().getLongitude()));
                                    mMarker.title("Booth Number : " + mBooth.getBoothNumber());
                                    mMarker.icon(BitmapDescriptorFactory.defaultMarker());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new com.google.android.gms.maps.model.LatLng(mBooth.getMapLocation().getLatitude(), mBooth.getMapLocation().getLongitude()), 16));
                                    mMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
                                 //   mMap.addMarker(mMarker);
                                }

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mProgressDialog = Constants.showDialog(this);


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
                                ActivityCompat.requestPermissions(AdminMap.this,
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
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
                        }
                    }
                });
    }
   /* @OnClick(R.id.btn_search)
    public void search(View v) {
        if (boothNumber.getText().toString().trim().length() == 0) {
            boothNumber.setError("Enter Booth Number");
        } else {
            searchByBooth(boothNumber.getText().toString().trim());
        }
    }*/

    private void searchByBooth(String boothNumber) {
        mDialog.show();
        mReference.orderByChild("boothNumber").equalTo(boothNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mVotersList = new ArrayList<>();
                mDialog.dismiss();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {

                    VoterPojo mPojo = indi.getValue(VoterPojo.class);
                    mVotersList.add(mPojo);
                }

                if (mVotersList.size() == 0) {
                    Constants.showToast("No Data Found", AdminMap.this);
                }
                else {
                    Constants.showToast(mVotersList.size()+" Voters Found", AdminMap.this);

                }
                placeMarkers(mMap, mVotersList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDialog.dismiss();

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.getUiSettings().setZoomControlsEnabled(true);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                VoterPojo mPojo = (VoterPojo) marker.getTag();
                if (mPojo != null) {
              //      Toast.makeText(getApplicationContext(), mPojo.getVoterName(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();

                }
                return false;
            }
        });

        mDialog.show();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();

                mVotersList = new ArrayList<>();

                for (DataSnapshot indi : dataSnapshot.getChildren()) {

                    VoterPojo mPojo = indi.getValue(VoterPojo.class);
                    mVotersList.add(mPojo);
                }

                placeMarkers(mMap, mVotersList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDialog.dismiss();

            }
        });
    }


    public void placeMarkers(GoogleMap mMap, ArrayList<VoterPojo> mVotersList) {

        for (int i = 0; i < mVotersList.size(); i++) {
            MarkerOptions mMarker = new MarkerOptions();
            mMarker.position(new LatLng(mVotersList.get(i).getLocation().getLatitude(), mVotersList.get(i).getLocation().getLongitude()));
            if (mVotersList.get(i).getSex().equalsIgnoreCase("Male")) {
                mMarker.icon(bitmapDescriptorFromVector(AdminMap.this, R.drawable.ic_man));

            } else {
                mMarker.icon(bitmapDescriptorFromVector(AdminMap.this, R.drawable.ic_girl));
            }
            mMarker.title((mVotersList.get(i).getVoterName()));
            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
            mMap.setInfoWindowAdapter(customInfoWindow);
            mMap.addMarker(mMarker).setTag(mVotersList.get(i));

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    final VoterPojo infoWindowData = (VoterPojo) marker.getTag();

                    dialContactPhone(infoWindowData.getMobileNumber());

                }
            });
        }

    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void loadBooths() {
        mProgressDialog.setMessage("Loading Booths");
        mProgressDialog.show();
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Booths/mBooths")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mProgressDialog.dismiss();
                        mBoothsList = new ArrayList<>();
                        boothNames.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Booth mBooth = snapshot.getValue(Booth.class);
                            mBoothsList.add(mBooth);
                            boothNames.add(mBooth.getBoothNumber() + "-" + mBooth.getName());
                        }
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, boothNames);
                        boothNumber.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mProgressDialog.dismiss();
                    }
                });
    }
}
