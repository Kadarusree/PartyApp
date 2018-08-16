package sree.myparty.Adapters;

import android.content.Context;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.maps.SupportMapFragment;

import java.io.Serializable;
import java.util.List;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.admin.MeetingAttendence;
import sree.myparty.admin.VolunteerProfile;
import sree.myparty.pojos.MeetingPojo;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 7/26/2018.
 */

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MyViewHolder>

{

    private Context mContext;
    private List<MeetingPojo> mMeetingsList;
    int selected_position = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, purpose, time, venue, meeting_for;
        final ImageView overflow;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.meeting_title);
            purpose = (TextView) view.findViewById(R.id.meeting_description);
            time = (TextView) view.findViewById(R.id.meeting_time);
            venue = (TextView) view.findViewById(R.id.meeting_venue);
            meeting_for = (TextView) view.findViewById(R.id.meeting_for);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public MeetingsAdapter(Context mContext, List<MeetingPojo> mMeetingsList) {
        this.mContext = mContext;
        this.mMeetingsList = mMeetingsList;
    }

    @Override
    public MeetingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_list_item, parent, false);

        return new MeetingsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MeetingPojo meeting = mMeetingsList.get(position);

        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.title.setTypeface(tf);
        holder.title.setText(meeting.getMeetingName());
        holder.purpose.setText("Purpose of Meeting : " + meeting.getMeetingPurpose());
        holder.time.setText("Date & Time : " + meeting.getMeetingDateTime());
        holder.venue.setText("Meeting Venue : " + meeting.getMeetingVenue());


        if (meeting.getIsForAll()) {
            holder.meeting_for.setText("Meeting For : All Users");
        } else {
            holder.meeting_for.setText("Meeting For : Volunteers");
        }
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                showPopupMenu(holder.overflow);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMeetingsList.size();
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();


        if (Constants.isAdmin){
            inflater.inflate(R.menu.meeting_options, popup.getMenu());
        }
        else {
            inflater.inflate(R.menu.meeting_user_options, popup.getMenu());
        }


        popup.setOnMenuItemClickListener(new MeetingsAdapter.MyMenuItemClickListener());
        popup.show();
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

                    Constants.selected_meeting = mMeetingsList.get(selected_position);
                    Intent i = new Intent(mContext, MeetingAttendence.class);
                    mContext.startActivity(i);
                    return true;
                case R.id.map:
                    launchMap(selected_position);
                    return true;
                case R.id.delete:
                    deleteMeeting(mMeetingsList.get(selected_position).getKey());
                    return true;
                default:
            }
            return false;
        }
    }

    public void launchMap(int selected_position) {
        if (mMeetingsList.get(selected_position).getMeetingLocation()!=null){
            Uri gmmIntentUri = Uri.parse("geo::0,0?q=" + mMeetingsList.get(selected_position).getMeetingLocation().getLatitude()
                    + "," + mMeetingsList.get(selected_position).getMeetingLocation().getLongitude()+"("+mMeetingsList.get(selected_position).getMeetingVenue()+")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(mapIntent);
            }
        }
        else {
            Toast.makeText(mContext,"Location Not Available",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteMeeting(String key){
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/Meetings").child(key).removeValue();
    }

}
