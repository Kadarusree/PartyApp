package sree.myparty.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sree.myparty.R;
import sree.myparty.beans.VolunteerLocationPojo;
import sree.myparty.pojos.IssueBean;
import sree.myparty.pojos.LatLng;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> times;
    private ArrayList<VolunteerLocationPojo> locations;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description, timestamp, createdby, booth_num;
        ImageView overflow;

        public MyViewHolder(View view) {
            super(view);

            booth_num = view.findViewById(R.id.da_booth_num);
            description = view.findViewById(R.id.da_description);
            createdby = view.findViewById(R.id.da_created_by);
            timestamp = view.findViewById(R.id.da_timestamp);
            overflow = view.findViewById(R.id.overflow);

        }
    }


    public LocationsAdapter(Context context, ArrayList<String> times, ArrayList<VolunteerLocationPojo> locations) {
        this.context = context;
        this.times = times;
        this.locations = locations;
    }

    @Override
    public LocationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue_adapter, parent, false);

        return new LocationsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationsAdapter.MyViewHolder holder, final int position) {
        final long time = Long.parseLong(times.get(position));
       /* holder.booth_num.setText("Booth Number :" + person.getBoothnumber()+"");
        holder.description.setText("Description :" + person.getDescription());
        holder.createdby.setText("Created By: " + person.getCreatedBy());*/
        holder.description.setText("Location :" + locations.get(position).getLocationName());
        holder.createdby.setText("Coordinates :" + locations.get(position).getLocation().getLatitude() + ", "
                + locations.get(position).getLocation().getLongitude());
        holder.timestamp.setText("Updated At : " + getDate(time, "dd/MM/yyyy HH:mm aa"));
        holder.overflow.setVisibility(View.GONE);
    }

    // recipe
    @Override
    public int getItemCount() {
        return times.size();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
