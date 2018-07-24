package sree.myparty.admin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sree.myparty.DashBoard.BaseActvity;
import sree.myparty.R;
import sree.myparty.volunteer.VolunteerHomeFragment;

public class AdminDashboard extends BaseActvity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        replaceFragement(new AdminHomeFragment());

    }
    public void replaceFragement(Fragment fg){
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.content_frame, fg);
        mFragmentTransaction.commit();
    }
}
