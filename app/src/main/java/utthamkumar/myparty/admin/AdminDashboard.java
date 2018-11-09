package utthamkumar.myparty.admin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import utthamkumar.myparty.DashBoard.BaseActvity;
import utthamkumar.myparty.R;
import utthamkumar.myparty.volunteer.VolunteerHomeFragment;

public class AdminDashboard extends AdminBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Voter 360° - ADMIN");

        replaceFragement(new AdminHomeFragment());

    }
    public void replaceFragement(Fragment fg){
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.content_frame, fg);
        mFragmentTransaction.commit();
    }
}
