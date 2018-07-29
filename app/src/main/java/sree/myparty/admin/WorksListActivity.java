package sree.myparty.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.R;
import sree.myparty.utils.ActivityLauncher;

public class WorksListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_list);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_add_workdone)
    public void launchWorkDone(View view) {
        ActivityLauncher.launchWorkDoneActivity(this);
    }
}
