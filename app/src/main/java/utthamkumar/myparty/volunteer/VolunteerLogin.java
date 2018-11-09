package utthamkumar.myparty.volunteer;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.pojos.VolunteerPojo;
import utthamkumar.myparty.session.SessionManager;
import utthamkumar.myparty.utils.ActivityLauncher;
import utthamkumar.myparty.utils.Constants;
import utthamkumar.myparty.utils.VolunteerSessionManager;

public class VolunteerLogin extends AppCompatActivity {
    @BindView(R.id.tv_vol_laningText)
    TextView mLandigText;

    @BindView(R.id.edt_vol_username)
    EditText edt_username;

    @BindView(R.id.edt_vol_password)
    EditText edt_password;

    VolunteerSessionManager Vol_mSessionManager;
    SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_login);

        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0\n Volunteer Login");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);

        Vol_mSessionManager = new VolunteerSessionManager(this);
        mSessionManager = new SessionManager(this);
        edt_username.setText(mSessionManager.getRegID());
        edt_username.setEnabled(false);

    }

    @OnClick(R.id.btn_vol_login)
    public void onButtonClick(View v) {
        MyApplication.getFirebaseDatabase().
                getReference(Constants.DB_PATH + "/Volunteers/" + mSessionManager.getRegID()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                              VolunteerPojo mVol =dataSnapshot.getValue(VolunteerPojo.class);
                            if (mVol.isAccepted()) {
                                if (edt_password.getText().toString().trim().equalsIgnoreCase(mVol.getPassword().trim())){
                                    Vol_mSessionManager.createUserSession(mVol);
                                    ActivityLauncher.volunteerDashboard(VolunteerLogin.this);
                                }
                                else {
                                    edt_password.setError("Wrong Password");
                                }
                            }
                            else {
                                Constants.showToast("Request Pending with Admin",VolunteerLogin.this);
                            }
                        }
                        else {
                            Constants.showToast("You must register first",VolunteerLogin.this);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
