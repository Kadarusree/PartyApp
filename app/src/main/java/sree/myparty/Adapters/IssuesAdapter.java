package sree.myparty.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sree.myparty.R;
import sree.myparty.pojos.CasteWiseVoterBean;
import sree.myparty.pojos.IssueBean;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.MyViewHolder> {
    private Context context;
    private List<IssueBean> personList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, timestamp, createdby, booth_num;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.da_title);
            booth_num = view.findViewById(R.id.da_booth_num);
         /*   description = view.findViewById(R.id.da_description);
            createdby = view.findViewById(R.id.da_created_by);
            timestamp = view.findViewById(R.id.da_timestamp);*/
        }
    }


    public IssuesAdapter(Context context, List<IssueBean> personList) {
        this.context = context;
        this.personList = personList;
    }

    @Override
    public IssuesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_adapter_item, parent, false);

        return new IssuesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IssuesAdapter.MyViewHolder holder, final int position) {
        final IssueBean person = personList.get(position);
        holder.name.setVisibility(View.GONE);
        holder.booth_num.setText("Booth Number :" + person.getBoothnumber()+"");
       /* holder.description.setText("Description :" + person.getDescription());
        holder.createdby.setText("Created By: " + person.getCreatedBy());
        holder.timestamp.setText("Posted At : "+getDate(person.getTimestamp(),"dd/MM/yyyy HH:mm aa"));
 */   }
    // recipe
    @Override
    public int getItemCount() {
        return personList.size();
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
