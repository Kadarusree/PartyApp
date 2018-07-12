package sree.myparty.volunteer;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

public class VolunteerRegis extends AppCompatActivity {
    @BindView(R.id.tv_vol_reg_laningText)
    TextView mLandigText;

    @BindView(R.id.vol_reg_booth_num)
    EditText mBoothNumber;

    @BindView(R.id.vol_reg_id)
    TextView mRegID;

    @BindView(R.id.vol_reg_password)
    EditText mPassword;

    SessionManager mSessionManager;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_regis);
        mSessionManager = new SessionManager(this);
        mProgressDialog = Constants.showDialog(this);

        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0\n Volunteer Registration");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);

        mRegID.setText("Reg ID. : " + mSessionManager.getRegID());


    }

    @OnClick(R.id.btn_vol_register)
    public void onButtonClick(View v) {
        if (validations()) {

            VolunteerPojo mVolunteer = new VolunteerPojo(mSessionManager.getName(),
                    mBoothNumber.getText().toString().trim(),
                    mSessionManager.getRegID(),
                    mPassword.getText().toString(),
                    mSessionManager.getFirebaseKey(),
                    mSessionManager.getProfilePic(),
                    mSessionManager.getQR(),
                    mSessionManager.getMobileNumber(),
                    false);
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

        if (mBoothNumber.getText().toString().length() < 1) {
            mBoothNumber.setError("Enter Booth Number");
        } else if (mPassword.getText().toString().trim().length() < 6) {
            mBoothNumber.setError("Password Must be Minimum 6 Characters");
        } else {
            isValid = true;
        }

        return isValid;
    }

}
