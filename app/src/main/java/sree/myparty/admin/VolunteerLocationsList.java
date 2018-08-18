package sree.myparty.admin;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sree.myparty.Adapters.LocationsAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.beans.VolunteerLocationPojo;
import sree.myparty.pojos.LatLng;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;
import sree.myparty.utils.RecyclerItemClickListener;

public class VolunteerLocationsList extends AppCompatActivity {


    @BindView(R.id.locationsList)
    RecyclerView locationsList;

    ArrayList<String> times;
    ArrayList<VolunteerLocationPojo> locations;

    LocationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_locations_list);

        ButterKnife.bind(this);
        times = new ArrayList<>();
        locations = new ArrayList<>();


        loadLocations(Constants.volunteer.getRegID());
        getSupportActionBar().setTitle("History : " + Constants.volunteer.getName());


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        locationsList.setLayoutManager(mLayoutManager);
        locationsList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        locationsList.setItemAnimator(new DefaultItemAnimator());

        locationsList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                launchMap(locations.get(position));
            }
        }));

    }

    public void launchMap(VolunteerLocationPojo location) {
        Uri gmmIntentUri = Uri.parse("geo::0,0?q=" + location.getLocation().getLatitude()
                + "," + location.getLocation().getLongitude() + "(" + location.getLocationName() + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public void loadLocations(String userID) {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/VolunteerLocations").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot dpst : dataSnapshot.getChildren()) {
                        times.add(dpst.getKey());


                        locations.add(dpst.getValue(VolunteerLocationPojo.class));
                    }


                    if (locations.size()==0){
                        Constants.showToast("No Data Found", VolunteerLocationsList.this);
                    }
                    mAdapter = new LocationsAdapter(VolunteerLocationsList.this, times, locations);
                    locationsList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
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

