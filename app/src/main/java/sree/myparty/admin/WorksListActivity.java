package sree.myparty.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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

        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/WorkDone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot indi : dataSnapshot.getChildren()){
                    WorkDonePojo workItem = indi.getValue(WorkDonePojo.class);
                    mWorkList.add(workItem);
                }
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @OnClick(R.id.btn_add_workdone)
    public void launchWorkDone(View view) {
        ActivityLauncher.launchWorkDoneActivity(this);
    }
}
