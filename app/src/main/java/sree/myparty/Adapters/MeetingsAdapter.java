package sree.myparty.Adapters;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;
import sree.myparty.R;
import sree.myparty.pojos.VolunteerPojo;

/**
 * Created by srikanthk on 7/26/2018.
 */

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MyViewHolder>

{

    private Context mContext;
    private List<VolunteerPojo> volunteerList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }


    public MeetingsAdapter(Context mContext, List<VolunteerPojo> volunteerList) {
        this.mContext = mContext;
        this.volunteerList = volunteerList;
    }

    @Override
    public MeetingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_list_item, parent, false);

        return new MeetingsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VolunteerPojo mVolunteer = volunteerList.get(position);
        holder.title.setText(mVolunteer.getName());

        if (mVolunteer.isAccepted()) {
            holder.count.setText("Approved");
            holder.count.setTextColor(mContext.getResources().getColor(R.color.color_green));
        } else {
            holder.count.setText("Approval Pending");
            holder.count.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        // loading album cover using Glide library
        Glide.with(mContext).load(mVolunteer.getProfilePic()).into(holder.thumbnail);


    }


    int selected_position = 0;


    /**
     * Showing popup menu when tapping on 3 dots
     */


    /**
     * Click listener for popup menu items
     */

    @Override
    public int getItemCount() {
        return volunteerList.size();
    }





}
