package sree.myparty.admin;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import sree.myparty.R;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

public class AdminHomeFragment extends Fragment implements View.OnClickListener{


    LinearLayout db_opt1,db_opt2,db_opt3,db_opt4,db_opt5,db_opt6,db_opt7,db_opt8,db_opt9,db_opt10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_home_fragment,null);

        db_opt1=(LinearLayout)v.findViewById(R.id.admin_db_opt1);
        db_opt2=(LinearLayout)v.findViewById(R.id.admin_db_opt2);
        db_opt3=(LinearLayout)v.findViewById(R.id.admin_db_opt3);
        db_opt4=(LinearLayout)v.findViewById(R.id.admin_db_opt4);
        db_opt5=(LinearLayout)v.findViewById(R.id.admin_db_opt5);
        db_opt6=(LinearLayout)v.findViewById(R.id.admin_db_opt6);
        db_opt7=(LinearLayout)v.findViewById(R.id.admin_db_opt7);
        db_opt8=(LinearLayout)v.findViewById(R.id.admin_db_opt8);
        db_opt9=(LinearLayout)v.findViewById(R.id.admin_db_opt9);
        db_opt10=(LinearLayout)v.findViewById(R.id.admin_db_opt10);



        db_opt1.setOnClickListener(this);
        db_opt2.setOnClickListener(this);
        db_opt4.setOnClickListener(this);
        db_opt3.setOnClickListener(this);
        db_opt5.setOnClickListener(this);
        db_opt6.setOnClickListener(this);
        db_opt7.setOnClickListener(this);
        db_opt8.setOnClickListener(this);
        db_opt9.setOnClickListener(this);
        db_opt10.setOnClickListener(this);



        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.admin_db_opt1:
                Constants.showToast("Coming Soon",getActivity());
                break;
            case R.id.admin_db_opt2:
                ActivityLauncher.launchMapActivity(getActivity());

                break;
            case R.id.admin_db_opt3:
                ActivityLauncher.influencePersons(getActivity());

                break;
            case R.id.admin_db_opt4:
                ActivityLauncher.localIssues(getActivity());

                break;
            case R.id.admin_db_opt5:
                Constants.showToast("Coming Soon",getActivity());

                break;
            case R.id.admin_db_opt6:
                Constants.showToast("Coming Soon",getActivity());

                break;
            case R.id.admin_db_opt7:
                ActivityLauncher.volunteersList(getActivity());

                break;
            case R.id.admin_db_opt8:
                ActivityLauncher.launchAttendenceActivity(getActivity());

                break;
            case R.id.admin_db_opt9:
                Constants.showToast("Coming Soon",getActivity());

                break;
            case R.id.admin_db_opt10:
                Constants.showToast("Coming Soon",getActivity());

                break;
        }

    }
}
