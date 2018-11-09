package utthamkumar.myparty.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import utthamkumar.myparty.Adapters.InfluencePersonAdapter;
import utthamkumar.myparty.Adapters.VolunteersListAdapter;
import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.constuecies.Booth;
import utthamkumar.myparty.pojos.Album;
import utthamkumar.myparty.pojos.InfluPerson;
import utthamkumar.myparty.pojos.VolunteerPojo;
import utthamkumar.myparty.pojos.VoterPojo;
import utthamkumar.myparty.utils.Constants;
import utthamkumar.myparty.utils.MyDividerItemDecoration;

public class VolunteerList extends AppCompatActivity {

    DatabaseReference mDbref;

    AlertDialog mDialog;

    private RecyclerView recyclerView;
    private List<VolunteerPojo> volunteerList;
    private VolunteersListAdapter mAdapter;

    private List<Album> albumList;


    ProgressDialog mProgressDialog;


    ArrayList<Booth> mBoothsList;
    ArrayList<String> boothNames;

    ArrayAdapter<String> adapter;
    @BindView(R.id.vol_reg_booth_num)
    Spinner mBoothNumber;


    @BindView(R.id.rb_all)
    RadioButton rb_all;

    @BindView(R.id.rb_p_booth)
    RadioButton rb_p_booth;


    @BindView(R.id.layout_booth)
    LinearLayout boothslayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_list);
        ButterKnife.bind(this);
        volunteerList = new ArrayList<>();

        mDialog = Constants.showDialog(this);

        mProgressDialog = Constants.showDialog(this);
        boothNames = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.list_volunteers);

        albumList = new ArrayList<>();
        mAdapter = new VolunteersListAdapter(this, volunteerList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        rb_all.setChecked(true);
        getAllVolunteers();
        boothslayout.setVisibility(View.GONE);
        rb_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rb_p_booth.setChecked(false);
                    volunteerList.clear();
                    getAllVolunteers();
                    boothslayout.setVisibility(View.GONE);

                }
            }
        });

        rb_p_booth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rb_all.setChecked(false);
                    volunteerList.clear();
                    loadBooths();
                    boothslayout.setVisibility(View.VISIBLE);

                }
            }
        });


        mBoothNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mBoothsList.size() > 0) {
                    getVolunteers(mBoothsList.get(position).getBoothNumber());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getVolunteers(String boothID) {
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Volunteers")
                .orderByChild("boothnumber").equalTo(boothID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        volunteerList.clear();
                        for (DataSnapshot indi : dataSnapshot.getChildren()) {
                            VolunteerPojo volItem = indi.getValue(VolunteerPojo.class);
                            volunteerList.add(volItem);
                        }

                        if (volunteerList.size() == 0) {
                            Constants.showToast("No Volunteers Found", VolunteerList.this);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void getAllVolunteers() {
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Volunteers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        volunteerList.clear();
                        for (DataSnapshot indi : dataSnapshot.getChildren()) {
                            VolunteerPojo volItem = indi.getValue(VolunteerPojo.class);
                            volunteerList.add(volItem);
                        }

                        if (volunteerList.size() == 0) {
                            Constants.showToast("No Volunteers Found", VolunteerList.this);
                        }
                        mAdapter.notifyDataSetChanged();
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


}
