package sree.myparty.admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sree.myparty.Adapters.BoothsAdapter;
import sree.myparty.Adapters.WorkDoneAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.constuecies.Booth;
import sree.myparty.pojos.WorkDonePojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;
import sree.myparty.utils.MyDividerItemDecoration;
import sree.myparty.utils.RecyclerItemClickListener;

public class BoothCommitteList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Booth> mBoothsList;
    private BoothsAdapter mAdapter;

    SessionManager mSessionManager;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth_committe_list);

        mSessionManager = new SessionManager(this);
        mProgressDialog = Constants.showDialog(this);

        recyclerView = findViewById(R.id.boothList);
        mBoothsList = new ArrayList<>();
        mAdapter = new BoothsAdapter(this, mBoothsList);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Constants.selected_booth_id = mBoothsList.get(position).getBoothNumber();
                ActivityLauncher.launchParticularBooth(BoothCommitteList.this);
            }
        }));


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        mProgressDialog.setMessage("Loading Booths");
        mProgressDialog.show();

        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Booths/mBooths").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();

                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    Booth mBooth = indi.getValue(Booth.class);
                    mBoothsList.add(mBooth);
                }
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       /* MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Booths/mBooths")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mBoothsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Booth mBooth = snapshot.getValue(Booth.class);
                            mBoothsList.add(mBooth);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mProgressDialog.dismiss();
                    }
                });*/
    }


    public void launchMap(View view) {
        ActivityLauncher.launchBoothsOnMap(this);
    }
}
