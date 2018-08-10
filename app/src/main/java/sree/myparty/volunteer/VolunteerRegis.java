package sree.myparty.volunteer;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.constuecies.Booth;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

public class VolunteerRegis extends AppCompatActivity {
    @BindView(R.id.tv_vol_reg_laningText)
    TextView mLandigText;

    @BindView(R.id.vol_reg_booth_num)
    Spinner mBoothNumber;

    @BindView(R.id.vol_reg_id)
    TextView mRegID;

    @BindView(R.id.vol_reg_password)
    EditText mPassword;

    SessionManager mSessionManager;
    ProgressDialog mProgressDialog;


    ArrayList<Booth> mBoothsList;
    ArrayList<String> boothNames;

    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_regis);
        mSessionManager = new SessionManager(this);
        mProgressDialog = Constants.showDialog(this);
        boothNames = new ArrayList<>();
        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0\n Volunteer Registration");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);


        mRegID.setText("Registration ID\n" + mSessionManager.getRegID());

        loadBooths();


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

    @OnClick(R.id.btn_vol_register)
    public void onButtonClick(View v) {
        if (validations()) {

            VolunteerPojo mVolunteer = new VolunteerPojo(mSessionManager.getName(),
                    mBoothsList.get(mBoothNumber.getSelectedItemPosition()).getBoothNumber(),
                    mSessionManager.getRegID(),
                    mPassword.getText().toString(),
                    mSessionManager.getFirebaseKey(),
                    mSessionManager.getProfilePic(),
                    mSessionManager.getQR(),
                    mSessionManager.getMobileNumber(),
                    false,"","");
            mProgressDialog.show();
            MyApplication.getFirebaseDatabase().
                    getReference(Constants.DB_PATH + "/Volunteers").
                    child(mSessionManager.getRegID()).setValue(mVolunteer).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Constants.showToast("Request sent to Admin", VolunteerRegis.this);
                    } else {
                        Constants.showToast("Failed", VolunteerRegis.this);

                    }
                }
            });
        }
    }

    public boolean validations() {
        boolean isValid = false;

        if (mPassword.getText().toString().trim().length() < 6) {
            mPassword.setError("Password Must be Minimum 6 Characters");
        } else if (mSessionManager.getProfilePic().length() < 10) {
            Constants.showToast("Upload your Profile Pic and Try Again", this);
        } else {
            isValid = true;
        }

        return isValid;
    }

}
