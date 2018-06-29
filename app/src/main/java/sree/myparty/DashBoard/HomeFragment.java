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

import sree.myparty.R;

/**
 * Created by srikanthk on 6/28/2018.
 */

public class HomeFragment extends Fragment {

     ImageView db_op1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.home_fragment,null,false);
        db_op1 = (ImageView)v.findViewById(R.id.db_op1);
        db_op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(getActivity());
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.id_card);
                d.show();
            }
        });
        return v;
    }
}
