package sree.myparty.volunteer;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import sree.myparty.R;
import sree.myparty.utils.ActivityLauncher;

public class VolunteerHomeFragment extends Fragment implements View.OnClickListener{



    ImageView db_op1,db_op2,db_op3,db_op4,db_op5,db_op6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.volunteer_home_fragment, null, false);

        db_op1 = (ImageView)v.findViewById(R.id.vol_db_op1);
        db_op2 = (ImageView)v.findViewById(R.id.vol_db_op2);
        db_op3 = (ImageView)v.findViewById(R.id.vol_db_op3);
       db_op4 = (ImageView)v.findViewById(R.id.vol_db_op4);
        db_op5 = (ImageView)v.findViewById(R.id.vol_db_op5);
      //  db_op6 = (ImageView)v.findViewById(R.id.vol_db_op6);*/

        db_op1.setOnClickListener(this);
        db_op2.setOnClickListener(this);
        db_op3.setOnClickListener(this);
        db_op4.setOnClickListener(this);
        db_op5.setOnClickListener(this);
      //  db_op6.setOnClickListener(this);*/


        return v;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vol_db_op1:
               ActivityLauncher.castewiseVoters(getActivity());
                break;
            case R.id.vol_db_op2:
                ActivityLauncher.influencePersons(getActivity());
                break;
            case R.id.vol_db_op3:
                ActivityLauncher.localIssues(getActivity());
                break;
            case R.id.vol_db_op4:
                ActivityLauncher.newsList(getActivity());
                break;
            case R.id.vol_db_op5:
                ActivityLauncher.postNews(getActivity());
                break;

        }
    }
}
