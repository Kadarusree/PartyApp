package utthamkumar.myparty.SliderIndicator;


import android.graphics.Color;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;

import utthamkumar.myparty.R;

public class SamplePagerAdapter extends PagerAdapter {

    private final Random random = new Random();
    private int mSize;
    FragmentActivity activity;


    public SamplePagerAdapter(int count) {
        mSize = count;
    }

    public SamplePagerAdapter(FragmentActivity activity) {

        mSize = 3;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        TextView textView = new TextView(view.getContext());
        ImageView image = new ImageView(view.getContext());
        View tagView=null;

        if (position == 0) {
            textView.setText("");
            image.setBackgroundResource(R.drawable.logout_vol);
            tagView = activity.getLayoutInflater().inflate(R.layout.induction_page1, null, false);
        }
        if (position == 1) {
            textView.setText("");
            image.setBackgroundResource(R.drawable.avatar);
            tagView = activity.getLayoutInflater().inflate(R.layout.induction_page2, null, false);

        }

        if (position == 2) {
            textView.setText("");
            image.setBackgroundResource(R.drawable.ic_logo);
            tagView = activity.getLayoutInflater().inflate(R.layout.induction_page3, null, false);

        }
        textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);



      /*    view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);*/
       // View tagView = activity.getLayoutInflater().inflate(R.layout.activity_admin_login, null, false);

//view.addView(tagView);

        view.addView(tagView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return tagView;
    }

    public void addItem() {
        mSize++;
        notifyDataSetChanged();
    }

    public void removeItem() {
        mSize--;
        mSize = mSize < 0 ? 0 : mSize;

        notifyDataSetChanged();
    }
}
