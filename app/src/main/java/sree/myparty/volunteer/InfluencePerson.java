package sree.myparty.volunteer;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import dmax.dialog.SpotsDialog;
import sree.myparty.Adapters.InfluencePersonAdapter;
import sree.myparty.DashBoard.Dashboard;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.beans.NewsPojo;
import sree.myparty.misc.NewsListAdapter;
import sree.myparty.pojos.InfluPerson;
import sree.myparty.utils.Constants;
import sree.myparty.utils.MyDividerItemDecoration;

public class InfluencePerson extends AppCompatActivity {

    @BindView(R.id.infl_name)
    EditText edt_name;
    @BindView(R.id.infl_booth_num)
    EditText booth_num;
    @BindView(R.id.infl_mobile_num)
    EditText edt_mobile_num;

    @BindView(R.id.btn_infl_save)
    Button btn_save;

    DatabaseReference mDbref;

    AlertDialog mDialog;

    private RecyclerView recyclerView;
    private List<InfluPerson> newsList;
    private InfluencePersonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influence_person);
        ButterKnife.bind(this);

        mDialog = Constants.showDialog(this);

        recyclerView = findViewById(R.id.list_influencePersons);
        newsList = new ArrayList<>();
        mAdapter = new InfluencePersonAdapter(this, newsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        mDialog.show();
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/InfluencePersons").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                newsList.clear();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    InfluPerson mNewsItem = indi.getValue(InfluPerson.class);
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

    @OnClick(R.id.btn_infl_save)
    public void onButtonClick(View v) {
        String name = edt_name.getText().toString().trim();
        String mobileNum = edt_mobile_num.getText().toString().trim();
        int boothNum = Integer.parseInt(booth_num.getText().toString());

        mDbref = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/InfluencePersons");
        String key = mDbref.push().getKey();
        InfluPerson mPerson = new InfluPerson(name, mobileNum, System.currentTimeMillis() + "", Constants.VOLUNTEER, boothNum);
        save(mPerson, mDbref, key);
    }

    public void save(InfluPerson mPerson, DatabaseReference mRef, String key) {
        mDialog.show();
        mRef.child(key).setValue(mPerson).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Constants.showToast("Added SucessFully", InfluencePerson.this);
                } else {
                    Constants.showToast("Failed To add", InfluencePerson.this);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mDialog.dismiss();

                Constants.showToast("Failed To add", InfluencePerson.this);

            }
        });

    }
}
