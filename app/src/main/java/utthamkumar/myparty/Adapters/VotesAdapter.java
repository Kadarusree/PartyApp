package utthamkumar.myparty.Adapters;

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

import java.util.ArrayList;
import java.util.List;

import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.admin.MeetingAttendence;
import utthamkumar.myparty.pojos.MeetingPojo;
import utthamkumar.myparty.pojos.VoterPojo;
import utthamkumar.myparty.utils.ActivityLauncher;
import utthamkumar.myparty.utils.Constants;

public class VotesAdapter extends RecyclerView.Adapter<VotesAdapter.MyViewHolder>

{

    private Context mContext;
    private List<VoterPojo> mVotersList;
    private List<String> mKeys;


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


    public VotesAdapter(Context mContext, List<VoterPojo> mVotersList, ArrayList<String> keys) {
        this.mContext = mContext;
        this.mVotersList = mVotersList;
        this.mKeys = keys;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        VoterPojo voter = mVotersList.get(position);

        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.title.setTypeface(tf);
        holder.title.setText(voter.getVoterName());
        holder.purpose.setText("Father Nameg : " + voter.getVoterFatherName());
        holder.time.setText("Voter ID : " + voter.getVoterID());
        holder.venue.setText("Booth Number : " + voter.getBoothNumber());
        holder.meeting_for.setText("Address : " + voter.getAddress());

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
        return mVotersList.size();
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.voter_edit, popup.getMenu());


        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
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
                case R.id.call:

                    dialContactPhone(mVotersList.get(selected_position).getMobileNumber());
                    return true;
                case R.id.edit:
                    Constants.voter = mVotersList.get(selected_position);
                    Constants.voter_key = mKeys.get(selected_position);
                    ActivityLauncher.launchVoterEdit(mContext);
                    return true;
                case R.id.delete:
                    deleteMeeting(mKeys.get(selected_position));
                    return true;
                default:
            }
            return false;
        }
    }

    private void dialContactPhone(final String phoneNumber) {
        mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void deleteMeeting(String key) {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Voters").child(key).removeValue();
    }
}
