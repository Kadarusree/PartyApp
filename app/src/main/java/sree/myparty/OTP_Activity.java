package sree.myparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;



import butterknife.ButterKnife;
import butterknife.OnClick;
import me.philio.pinentry.PinEntryView;
import sree.myparty.DashBoard.Dashboard;

public class OTP_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        ProgressBar mProgressbar = (ProgressBar)findViewById(R.id.progressBar);
        mProgressbar.setVisibility(View.VISIBLE);


        final PinEntryView pinEntry = (PinEntryView) findViewById(R.id.inputOtp);

    }

    @OnClick(R.id.btn_verify_otp)
    public void onButtonClick(View view) {
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
