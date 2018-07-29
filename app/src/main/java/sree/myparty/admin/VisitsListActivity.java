package sree.myparty.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.R;
import sree.myparty.utils.ActivityLauncher;

public class VisitsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_list);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_add_visit)
    public void launchVisits(View view) {
        ActivityLauncher.launchVisitActivity(this);
    }
}
