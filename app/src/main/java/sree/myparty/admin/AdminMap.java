package sree.myparty.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
    EditText boothNumber;

    ProgressDialog mDialog;
/*
    @BindView(R.id.top_bar_activity_toolbar)
    Toolbar toolbar;*/

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
                }
            }
        });
    }

    @OnClick(R.id.btn_search)
    public void search(View v) {
        if (boothNumber.getText().toString().trim().length() == 0) {
            boothNumber.setError("Enter Booth Number");
        } else {
            searchByBooth(boothNumber.getText().toString().trim());
        }
    }

    private void searchByBooth(String boothNumber) {
        mDialog.show();
        mReference.orderByChild("boothNumber").equalTo(boothNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mVotersList = new ArrayList<>();
                mDialog.dismiss();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {

                    VoterPojo mPojo = indi.getValue(VoterPojo.class);
                    mVotersList.add(mPojo);
                }

                mMap.clear();
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(17.37, 78.40);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sirpur"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(17.37, 78.40), 12));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                VoterPojo mPojo = (VoterPojo) marker.getTag();
                if (mPojo != null) {
                    Toast.makeText(getApplicationContext(), mPojo.getVoterName(), Toast.LENGTH_LONG).show();
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
        }

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
}
