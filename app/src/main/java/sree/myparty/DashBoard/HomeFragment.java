package sree.myparty.DashBoard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sree.myparty.R;

/**
 * Created by srikanthk on 6/28/2018.
 */

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       
        return inflater.inflate(R.layout.home_fragment,null,false);
    }
}
