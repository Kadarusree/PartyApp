package utthamkumar.myparty.volunteer;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.Adapters.IssuesAdapter;
import utthamkumar.myparty.pojos.CasteWiseVoterBean;
import utthamkumar.myparty.pojos.IssueBean;
import utthamkumar.myparty.session.SessionManager;
import utthamkumar.myparty.utils.Constants;
import utthamkumar.myparty.utils.MyDividerItemDecoration;
import utthamkumar.myparty.utils.VolunteerSessionManager;

public class Local_Issues extends AppCompatActivity {


    @BindView(R.id.issue_boothNumber)
    EditText edt_BoothNum;

    @BindView(R.id.issue_description)
    EditText edt_description;

    DatabaseReference mReference;
    AlertDialog mDialog;


    private RecyclerView recyclerView;
    private List<IssueBean> newsList;
    private IssuesAdapter mAdapter;

    SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local__issues);
        ButterKnife.bind(this);
        mReference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/LocalIssues");
        mDialog = Constants.showDialog(this);
        mSessionManager = new SessionManager(this);
        edt_BoothNum.setText(mSessionManager.getBoothNumber());
        edt_BoothNum.setEnabled(false);
        mDialog = Constants.showDialog(this);

        recyclerView = findViewById(R.id.issue_list);
        newsList = new ArrayList<>();
        mAdapter = new IssuesAdapter(this, newsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        mDialog.show();
        mReference.orderByChild("boothnumber")
                .equalTo(Integer.parseInt(mSessionManager.getBoothNumber()))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                newsList.clear();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    IssueBean mNewsItem = indi.getValue(IssueBean.class);
                    newsList.add(mNewsItem);
                }
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();

                // stop animating Shimmer and hide the layout

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDialog.dismiss();
            }
        });
    }


    @OnClick(R.id.btn_post_issue)
    public void onButtonClick(View v) {
        if (edt_BoothNum.getText().toString().trim().equalsIgnoreCase("")) {
            edt_BoothNum.setError("Booth number required");
        } else if (edt_description.getText().toString().trim().equalsIgnoreCase("")) {
            edt_description.setError("Add your issues description");
        } else {
            String desc = edt_description.getText().toString().trim();

            int boothNum = Integer.parseInt(edt_BoothNum.getText().toString().trim());

            IssueBean mIssue = new IssueBean(boothNum, System.currentTimeMillis(), desc, new VolunteerSessionManager(getApplicationContext()).getVolName());

            save(mIssue);
        }


    }

    public void save(IssueBean mIssue) {
        String key = mReference.push().getKey();
        mDialog.show();
        mReference.child(key).setValue(mIssue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    edt_BoothNum.setText("");
                    edt_description.setText("");
                    Constants.showToast("Issue Posted", Local_Issues.this);
                } else {
                    Constants.showToast("Failed To add", Local_Issues.this);
                }
            }
        });
    }
}
