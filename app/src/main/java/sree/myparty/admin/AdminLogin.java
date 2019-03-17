package sree.myparty.admin;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.ACAdminPojo;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;
import sree.myparty.volunteer.VolunteerLogin;

public class AdminLogin extends AppCompatActivity {
    @BindView(R.id.tv_admin_laningText)
    TextView mLandigText;

    @BindView(R.id.edt_admin_username)
    EditText edt_username;

    @BindView(R.id.edt_admin_password)
    EditText edt_password;

    @BindView(R.id.rb_master)
    CheckBox rb_master;

    SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0 Admin Login");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);

        mSessionManager = new SessionManager(AdminLogin.this);
        edt_username.setText(mSessionManager.getRegID());
        edt_username.setEnabled(false);

        rb_master.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_username.setText("");
                    edt_username.setEnabled(true);

                } else {
                    edt_username.setText(mSessionManager.getRegID());
                    edt_username.setEnabled(false);
                }
            }
        });
    }

    @OnClick(R.id.btn_admin_login)
    public void Login(View v) {

      /* Constants.isAdmin = true;
        ActivityLauncher.launchAdminDB(AdminLogin.this);*/

        if (rb_master.isChecked()) {
            Constants.isMaster = true;

            if (edt_username.getText().toString().trim().equalsIgnoreCase("Mahesh")&&edt_password.getText().toString().trim().equals("2011")){
                Constants.isAdmin = true;
                ActivityLauncher.launchAdminDB(AdminLogin.this);
            }
            else{
                Constants.showToast("Invalid Credentials",this);
            }
        }

        else{
            Constants.isMaster = false;
            MyApplication.getFirebaseDatabase().
                    getReference(Constants.DB_PATH + "/Admins/" + mSessionManager.getRegID()).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                ACAdminPojo mVol =dataSnapshot.getValue(ACAdminPojo.class);
                                if (mVol.isAccepted()) {
                                    if (edt_password.getText().toString().trim().equalsIgnoreCase(mVol.getPassword().trim())){
                                        ActivityLauncher.launchAdminDB(AdminLogin.this);
                                    }
                                    else {
                                        edt_password.setError("Wrong Password");
                                    }
                                }
                                else {
                                    Constants.showToast("Request Pending with Admin",AdminLogin.this);
                                }
                            }
                            else {
                                Constants.showToast("You must register first",AdminLogin.this);

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }


    }
}
