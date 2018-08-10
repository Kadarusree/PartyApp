package sree.myparty.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.admin.MeetingAttendence;
import sree.myparty.pojos.MeetingPojo;
import sree.myparty.pojos.WorkDonePojo;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 8/1/2018.
 */

public class WorkDoneAdapter extends RecyclerView.Adapter<WorkDoneAdapter.MyViewHolder>

{

    private Context mContext;
    private List<WorkDonePojo> mWorksList;
    int selected_position = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, purpose, time, venue, meeting_for;
        public ImageView thumbnail, overflow;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.meeting_title);
            purpose = (TextView) view.findViewById(R.id.meeting_description);
            time = (TextView) view.findViewById(R.id.meeting_time);
            venue = (TextView) view.findViewById(R.id.meeting_venue);
            meeting_for = (TextView) view.findViewById(R.id.meeting_for);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }


    public WorkDoneAdapter(Context mContext, List<WorkDonePojo> mWorksList) {
        this.mContext = mContext;
        this.mWorksList = mWorksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workdone_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        WorkDonePojo work = mWorksList.get(position);

        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.title.setTypeface(tf);
        holder.title.setText(work.getWorkName());
        holder.purpose.setText("Area Name : " + work.getWorkArea()+" Booth Number : "+work.getBoothNumber());
        holder.time.setText("Work Done From : " + work.getStartDate() +" To "+work.getEndDate() );
        holder.venue.setText("Money Spent  : " + work.getMoneySpent());

        holder.meeting_for.setText("Work Done By : "+work.getSupervisor() + " Ph. No : "+work.getContactNumber());
        holder.overflow.setVisibility(View.INVISIBLE);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                showPopupMenu(holder.overflow);
            }
        });

        loadImage(mContext,holder.thumbnail,work.getWorkImage());
    }


    @Override
    public int getItemCount() {
        return mWorksList.size();
    }


    private void showPopupMenu(View view) {


     /*   // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.meeting_options, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();*/
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.attendence:
                    return true;
                case R.id.map:
                    return true;
                case R.id.delete:
                    return true;
                default:
            }
            return false;
        }
    }

    public static void loadImage(final Context context, ImageView imageView, String url) {


        //placeHolderUrl=R.drawable.ic_user;
        //errorImageUrl=R.drawable.ic_error;
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .placeholder(R.drawable.avatar) //this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.
                .error(R.drawable.avatar)//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.ALL) //using to load into cache then second time it will load fast.
                .animate(R.anim.fade_in) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                .fitCenter()//this method help to fit image into center of your ImageView
                .into(imageView); //pass imageView reference to appear the image.
    }
}
