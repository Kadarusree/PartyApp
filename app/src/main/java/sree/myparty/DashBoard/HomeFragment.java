package sree.myparty.DashBoard;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import butterknife.BindView;
import sree.myparty.R;
import sree.myparty.utils.ActivityLauncher;

/**
 * Created by srikanthk on 6/28/2018.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

     ImageView db_op1,db_op2,db_op3,db_op4,db_op5,db_op6;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.home_fragment,null,false);

        db_op1 = (ImageView)v.findViewById(R.id.db_op1);
        db_op2 = (ImageView)v.findViewById(R.id.db_op2);
        db_op3 = (ImageView)v.findViewById(R.id.db_op3);
        db_op4 = (ImageView)v.findViewById(R.id.db_op4);
        db_op5 = (ImageView)v.findViewById(R.id.db_op5);
        db_op6 = (ImageView)v.findViewById(R.id.db_op6);

        db_op1.setOnClickListener(this);
        db_op2.setOnClickListener(this);
        db_op3.setOnClickListener(this);
        db_op4.setOnClickListener(this);
        db_op5.setOnClickListener(this);
        db_op6.setOnClickListener(this);



        db_op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.db_op1:
                Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.id_card);
                d.show();
                break;
            case R.id.db_op2:
                ActivityLauncher.volunteerRegistartionScreen(getActivity());
                break;
            case R.id.db_op3:
                break;
            case R.id.db_op4:
                break;
            case R.id.db_op5:
                ActivityLauncher.volunteerLoginScreen(getActivity());

                break;
            case R.id.db_op6:
                ActivityLauncher.adminLogin(getActivity());

                break;
        }
    }
}
