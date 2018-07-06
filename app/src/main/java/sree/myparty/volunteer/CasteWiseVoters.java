package sree.myparty.volunteer;

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
import sree.myparty.Adapters.CasteWiseVotersAdapter;
import sree.myparty.Adapters.InfluencePersonAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.CasteWiseVoterBean;
import sree.myparty.pojos.InfluPerson;
import sree.myparty.utils.Constants;
import sree.myparty.utils.MyDividerItemDecoration;

public class CasteWiseVoters extends AppCompatActivity {


    @BindView(R.id.cwv_name)
    EditText edt_name;

    @BindView(R.id.cwv_voterID)
    EditText edt_voterID;

    @BindView(R.id.cwv_boothnum)
    EditText edt_BoothNum;

    @BindView(R.id.cwv_caste)
    EditText edt_caste;

    DatabaseReference mReference;
    AlertDialog mDialog;

    private RecyclerView recyclerView;
    private List<CasteWiseVoterBean> newsList;
    private CasteWiseVotersAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caste_wise_voters);
        ButterKnife.bind(this);
        mReference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/CasteWiseVoters");
    mDialog = Constants.showDialog(this);

        recyclerView = findViewById(R.id.list_castewiseVoters);
        newsList = new ArrayList<>();
        mAdapter = new CasteWiseVotersAdapter(this, newsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        mDialog.show();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                newsList.clear();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    indi.getKey();


                    CasteWiseVoterBean mNewsItem = indi.getValue(CasteWiseVoterBean.class);
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


    @OnClick(R.id.btn_cwv_save)
    public void onButtonClick(View v){


        String name = edt_name.getText().toString();
        String voterID = edt_voterID.getText().toString();
        String caste = edt_caste.getText().toString().trim();

        int boothNum = Integer.parseInt(edt_BoothNum.getText().toString().trim());

        CasteWiseVoterBean mVoter = new CasteWiseVoterBean(name,voterID,caste,System.currentTimeMillis()+"",Constants.VOLUNTEER,boothNum);
        save(mVoter);
    }

    public void save(CasteWiseVoterBean mVoter){
        String key = mReference.push().getKey();
        mDialog.show();
        mReference.child(mVoter.getCaste()).child(key).setValue(mVoter).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Constants.showToast("Added SucessFully", CasteWiseVoters.this);
                }
                else {
                    Constants.showToast("Failed To add", CasteWiseVoters.this);
                }
            }
        });
    }
}
