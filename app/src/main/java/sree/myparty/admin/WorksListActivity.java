package sree.myparty.admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.Adapters.WorkDoneAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.beans.NewsPojo;
import sree.myparty.constuecies.Booth;
import sree.myparty.misc.NewsListAdapter;
import sree.myparty.pojos.WorkDonePojo;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;
import sree.myparty.utils.MyDividerItemDecoration;

public class WorksListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<WorkDonePojo> mWorkList;
    private WorkDoneAdapter mAdapter;


    @BindView(R.id.btn_add_workdone)
    Button btn_add_workdone;

    ProgressDialog mProgressDialog;


    ArrayList<Booth> mBoothsList;
    ArrayList<String> boothNames;

    ArrayAdapter<String> adapter;
    @BindView(R.id.vol_reg_booth_num)
    Spinner mBoothNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_list);
        ButterKnife.bind(this);

        if (!Constants.isAdmin){
            btn_add_workdone.setVisibility(View.GONE);
        }

        recyclerView = findViewById(R.id.worksList);
        mWorkList = new ArrayList<>();
        mAdapter = new WorkDoneAdapter(this, mWorkList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        mProgressDialog = Constants.showDialog(this);
        boothNames = new ArrayList<>();
        loadBooths();
        mBoothNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mBoothsList.size()>0){
                    getWorks(mBoothsList.get(position).getBoothNumber());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @OnClick(R.id.btn_add_workdone)
    public void launchWorkDone(View view) {
        ActivityLauncher.launchWorkDoneActivity(this);
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

    public void getWorks(String boothID){
        mProgressDialog.show();
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/WorkDone").orderByChild("boothNumber").equalTo(boothID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mWorkList.clear();
                mProgressDialog.dismiss();
                for (DataSnapshot indi : dataSnapshot.getChildren()){
                    WorkDonePojo workItem = indi.getValue(WorkDonePojo.class);
                    mWorkList.add(workItem);
                }
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();

                if (mWorkList.size()==0){
                    Constants.showToast("Data Not Found",WorksListActivity.this);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });
    }
}
