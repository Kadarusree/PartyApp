package sree.myparty.admin;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.Adapters.VolunteersListAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.constuecies.Booth;
import sree.myparty.pojos.LatLng;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.utils.Constants;

public class ParticularBooth extends AppCompatActivity implements OnMapReadyCallback {
    int PLACE_PICKER_REQUEST = 1;
    LatLng addedLocation;
    String key;


    @BindView(R.id.booth_number_name)
    TextView booth_number_name;

    @BindView(R.id.booth_adress)
    TextView booth_adress;

    @BindView(R.id.booth_commitee_status_count)
    TextView booth_commitee_status_count;

    private RecyclerView recyclerView;
    private List<VolunteerPojo> volunteerList;
    private VolunteersListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_booth);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        volunteerList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.list_volunteers);

        mAdapter = new VolunteersListAdapter(this, volunteerList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getBoothCommiteeMembers();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Booths/mBooths")
                .orderByChild("boothNumber")
                .equalTo(Constants.selected_booth_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        key = child.getKey();
                        Booth mBooth = child.getValue(Booth.class);

                        booth_adress.setText("Address : "+mBooth.getLocation());
                        booth_number_name.setText("Booth  Number : "+mBooth.getBoothNumber());


                        if (mBooth.getMapLocation() != null) {
                            com.google.android.gms.maps.model.LatLng markerLocation = new com.google.android.gms.maps.model.LatLng(mBooth.getMapLocation().getLatitude(), mBooth.getMapLocation().getLongitude());
                            MarkerOptions mMarker = new MarkerOptions();
                            mMarker.position(markerLocation);
                            mMarker.title(mBooth.getName());
                            googleMap.addMarker(mMarker);

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation,12));
                            googleMap.animateCamera(CameraUpdateFactory.zoomIn());// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
                        }
                        else {
                            Constants.showToast("Location Not Avaialble",ParticularBooth.this);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.pick_loaction)
    public void launchPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                addedLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                MyApplication.getFirebaseDatabase()
                        .getReference(Constants.DB_PATH + "/Booths/mBooths")
                        .child(key).child("mapLocation").setValue(addedLocation);

               /* String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
            }
        }
    }

    public void getBoothCommiteeMembers(){

            MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/Volunteers").orderByChild("boothnumber").equalTo(Constants.selected_booth_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    volunteerList.clear();
                    if (dataSnapshot.getChildrenCount()>0){
                        booth_commitee_status_count.setText("Booth Committee Formed with "+dataSnapshot.getChildrenCount()+" Members");

                        for (DataSnapshot indi : dataSnapshot.getChildren()) {
                            VolunteerPojo volItem = indi.getValue(VolunteerPojo.class);
                            volunteerList.add(volItem);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    else {
                        booth_commitee_status_count.setText("Booth Committee Not Formed");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
