package sree.myparty.volunteer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sree.myparty.DashBoard.BaseActvity;
import sree.myparty.DashBoard.HomeFragment;
import sree.myparty.R;

public class VolunteerDashboard extends VolunteerBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        replaceFragement(new VolunteerHomeFragment());

        getSupportActionBar().setTitle("Volunteer");

    }
    public void replaceFragement(Fragment fg){
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.content_frame, fg);
        mFragmentTransaction.commit();
    }
}
