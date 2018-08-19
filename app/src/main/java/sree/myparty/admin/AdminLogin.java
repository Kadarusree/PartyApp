package sree.myparty.admin;

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
import sree.myparty.utils.Constants;

public class AdminLogin extends AppCompatActivity {
    @BindView(R.id.tv_admin_laningText)
    TextView mLandigText;

    @BindView(R.id.edt_admin_username)
    EditText edt_username;

    @BindView(R.id.edt_admin_password)
    EditText edt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0\n Admin Login");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);
    }

    @OnClick(R.id.btn_admin_login)
    public void Login(View v){

      /* Constants.isAdmin = true;
        ActivityLauncher.launchAdminDB(AdminLogin.this);*/
     if (edt_username.getText().toString().trim().equalsIgnoreCase("Mahesh")&&edt_password.getText().toString().trim().equals("2011")){
            Constants.isAdmin = true;
            ActivityLauncher.launchAdminDB(AdminLogin.this);
        }
        else{
            Constants.showToast("Invalid Credentials",this);
        }
    }
}
