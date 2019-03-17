package sree.myparty.admin;

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
import sree.myparty.pojos.ACAdminPojo;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;
import sree.myparty.volunteer.VolunteerRegis;

public class AC_Admin_Application extends AppCompatActivity {


    @BindView(R.id.tv_vol_laningText)
    TextView mLandigText;


    @BindView(R.id.edt_vol_username)
    EditText edt_username;

    @BindView(R.id.edt_vol_password)
    EditText edt_password;

    @BindView(R.id.ac_admin_appln_pc)
    TextView tv_pc;

    @BindView(R.id.ac_admin_appln_ac)
    TextView tv_ac;

    @BindView(R.id.ac_admin_appln_booth)
    TextView tv_booth;

    SessionManager mSessionManager;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac__admin__application);

        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0\n Admin Registration");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);

        mSessionManager = new SessionManager(this);
        mProgressDialog = Constants.showDialog(this);

        tv_pc.setText("PARLIMENT CONST : "+mSessionManager.getPC_NAME());
        tv_ac.setText("ASSEMBLY CONST : "+mSessionManager.getAC_NAME());
        tv_booth.setText("BOOTH NUMBER : "+mSessionManager.getBoothNumber());

        edt_username.setText(mSessionManager.getRegID());
        edt_username.setEnabled(false);
    }

    @OnClick(R.id.btn_vol_register)
    public void onButtonClick(View v) {
        if (validations()) {

            ACAdminPojo mVolunteer = new ACAdminPojo(mSessionManager.getName(),
                    mSessionManager.getBoothNumber(),
                    mSessionManager.getRegID(),
                    edt_password.getText().toString(),
                    mSessionManager.getFirebaseKey(),
                    mSessionManager.getProfilePic(),
                    mSessionManager.getQR(),
                    mSessionManager.getMobileNumber(),
                    false);
            mProgressDialog.show();
            MyApplication.getFirebaseDatabase().
                    getReference(Constants.DB_PATH + "/Admins").
                    child(mSessionManager.getRegID()).setValue(mVolunteer).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Constants.showToast("Request sent to Master Admin", AC_Admin_Application.this);
                    } else {
                        Constants.showToast("Failed", AC_Admin_Application.this);
                    }
                }
            });
        }
    }
    public boolean validations() {
        boolean isValid = false;

        if (edt_password.getText().toString().trim().length() < 6) {
            edt_password.setError("Password Must be Minimum 6 Characters");
        } else if (mSessionManager.getProfilePic().length() < 10) {
            Constants.showToast("Upload your Profile Pic and Try Again", this);
        } else {
            isValid = true;
        }

        return isValid;
    }
}
