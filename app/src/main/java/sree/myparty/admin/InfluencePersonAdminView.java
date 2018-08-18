package sree.myparty.admin;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sree.myparty.Adapters.InfluencePersonAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.constuecies.Booth;
import sree.myparty.pojos.InfluPerson;
import sree.myparty.utils.Constants;
import sree.myparty.volunteer.InfluencePerson;

public class InfluencePersonAdminView extends AppCompatActivity {
    ProgressDialog mProgressDialog;


    ArrayList<Booth> mBoothsList;
    ArrayList<String> boothNames;

    ArrayAdapter<String> adapter;
    @BindView(R.id.vol_reg_booth_num)
    Spinner mBoothNumber;

    private RecyclerView recyclerView;
    private List<InfluPerson> newsList;
    private InfluencePersonAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influence_person_admin_view);

        ButterKnife.bind(this);
        mProgressDialog = Constants.showDialog(this);
        boothNames = new ArrayList<>();
        recyclerView = findViewById(R.id.list_influencePersons);
        newsList = new ArrayList<>();
        mAdapter = new InfluencePersonAdapter(this, newsList);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        loadBooths();

        mBoothNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mBoothsList.size()>0){
                   getPersons(mBoothsList.get(position).getBoothNumber());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getPersons(String boothNumber) {
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/InfluencePersons")
                .orderByChild("boothNumber").equalTo(Integer.parseInt(boothNumber))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                newsList.clear();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    InfluPerson mNewsItem = indi.getValue(InfluPerson.class);
                    newsList.add(mNewsItem);
                }
                // refreshing recycler view
                if (newsList.size()==0){
                    Constants.showToast("No Data Found",InfluencePersonAdminView.this);
                }
                else {
                    Constants.showToast(newsList.size()+" Persons Found",InfluencePersonAdminView.this);

                }
                mAdapter.notifyDataSetChanged();

                // stop animating Shimmer and hide the layout

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });
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
                        mBoothNumber.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mProgressDialog.dismiss();
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
