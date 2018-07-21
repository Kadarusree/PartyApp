package sree.myparty.admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.VoterPojo;
import sree.myparty.utils.Constants;

public class AdminMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    DatabaseReference mReference;

    ArrayList<VoterPojo> mVotersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mReference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Voters");
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(19.483, 79.60);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sirpur"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(17.37, 78.40), 15));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                VoterPojo mPojo = (VoterPojo) marker.getTag();
                Toast.makeText(getApplicationContext(), mPojo.getVoterName(), Toast.LENGTH_LONG).show();
                return false;
            }
        });


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mVotersList = new ArrayList<>();

                for (DataSnapshot indi : dataSnapshot.getChildren()) {

                    VoterPojo mPojo = indi.getValue(VoterPojo.class);
                    mVotersList.add(mPojo);
                }

                placeMarkers(mMap, mVotersList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
            mMap.addMarker(mMarker).setTag(mVotersList.get(i));
        }

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth() , vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
