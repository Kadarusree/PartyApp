package utthamkumar.myparty.SliderIndicator;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import utthamkumar.myparty.R;



public class CustomAnimationFragment extends Fragment {
    Button btnRegister;
    IntroActivity actvity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_custom_animation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new SamplePagerAdapter(getActivity()));
        indicator.setViewPager(viewpager);
        actvity=(IntroActivity)getActivity();



      // btnLogin=(Button)view.findViewById(R.id.btnLogin);
        btnRegister=(Button)view.findViewById(R.id.btnRegister);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              actvity.replaceFragment(1);
            }
        });


    }
}