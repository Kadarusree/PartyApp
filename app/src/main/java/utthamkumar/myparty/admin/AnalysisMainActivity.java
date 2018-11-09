package utthamkumar.myparty.admin;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utthamkumar.myparty.R;
import utthamkumar.myparty.utils.ActivityLauncher;

public class AnalysisMainActivity extends AppCompatActivity {


    @BindView(R.id.a_tv1)
    TextView tv1;

    @BindView(R.id.a_tv2)
    TextView tv2;

    @BindView(R.id.a_tv3)
    TextView tv3;
    @BindView(R.id.a_tv4)
    TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_main);
        ButterKnife.bind(this);
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        tv1.setTypeface(tf);
        tv2.setTypeface(tf);

        tv3.setTypeface(tf);
        tv4.setTypeface(tf);


    }


    @OnClick(R.id.cv1)
    public void VotersAnalysis(View view) {
        ActivityLauncher.launchVotersAnalysis(this);
    }

    @OnClick(R.id.cv2)
    public void VotesAnalysis(View view) {
        ActivityLauncher.launchVotesAnalysis(this);

    }

    @OnClick(R.id.cv3)
    public void BoothWiseAnalysis(View view) {
        ActivityLauncher.launchBoothList(this);
    }

    @OnClick(R.id.cv4)
    public void presentTrend(View view) {
        ActivityLauncher.launchPresentTrend(this);
    }
}
