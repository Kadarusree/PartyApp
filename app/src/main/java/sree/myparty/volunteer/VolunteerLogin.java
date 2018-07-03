package sree.myparty.volunteer;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.R;
import sree.myparty.utils.ActivityLauncher;

public class VolunteerLogin extends AppCompatActivity {
    @BindView(R.id.tv_vol_laningText)
    TextView mLandigText;

    @BindView(R.id.edt_vol_username)
    EditText edt_username;

    @BindView(R.id.edt_vol_password)
    EditText edt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_login);

        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0\n Volunteer Login");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);
    }

    @OnClick(R.id.btn_vol_login)
    public void onButtonClick(View v) {
        ActivityLauncher.volunteerDashboard(VolunteerLogin.this);
    }
}
