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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.admin.VolunteerProfile;
import sree.myparty.beans.NewsPojo;
import sree.myparty.misc.NewsListAdapter;
import sree.myparty.pojos.InfluPerson;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.utils.Constants;

public class InfluencePersonAdapter extends RecyclerView.Adapter<InfluencePersonAdapter.MyViewHolder> {
    private Context context;
    private List<InfluPerson> personList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, booth_num;
        public ImageView thumbnail, overflow;
/*
TextView description, timestamp, createdby;*/


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.da_title);
            booth_num = view.findViewById(R.id.da_booth_num);
            /*description = view.findViewById(R.id.da_description);
            createdby = view.findViewById(R.id.da_created_by);
            timestamp = view.findViewById(R.id.da_timestamp);*/

            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public InfluencePersonAdapter(Context context, List<InfluPerson> personList) {
        this.context = context;
        this.personList = personList;
    }

    @Override
    public InfluencePersonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_adapter_item, parent, false);

        return new InfluencePersonAdapter.MyViewHolder(itemView);
    }

    int selected_position = 0;

    @Override
    public void onBindViewHolder(final InfluencePersonAdapter.MyViewHolder holder, final int position) {
        final InfluPerson person = personList.get(position);
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        holder.name.setTypeface(tf);
        holder.name.setText(person.getName());
        holder.booth_num.setText("Booth Number :" + person.getBoothNumber() + "");
       /* holder.description.setText("Mobile Number :" + person.getMobileNumber());
        holder.createdby.setText("Added By: " + person.getAddedBy());
        holder.timestamp.setText("Added At : "+getDate(Long.parseLong(person.getTimestamp()),"dd/MM/yyyy HH:mm aa")+"");
*/
        loadImage(context, holder.thumbnail, person.getProfile_pic());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                showPopupMenu(holder.overflow);
            }
        });

    }

    // recipe
    @Override
    public int getItemCount() {
        return personList.size();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        //Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.influence_person, popup.getMenu());
        popup.setOnMenuItemClickListener(new InfluencePersonAdapter.MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.map:
                    launchMap(selected_position);
                    return true;
                case R.id.call:
                    dialContactPhone(personList.get(selected_position).getMobileNumber());
                    return true;
                case R.id.details:
                    Toast.makeText(context, "Added By: " + personList.get(selected_position).getAddedBy()
                            + "On "
                            + getDate(Long.parseLong(personList.get(selected_position).getTimestamp()), "dd/MM/yyyy HH:mm aa"), Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete:
                    DeletePerson(personList.get(selected_position).getReg_id());
                    return true;
                default:
            }
            return false;
        }
    }

    public void DeletePerson(String key) {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/InfluencePersons").child(key).removeValue();
        MyApplication.getFirebaseStorage().getReference(Constants.DB_PATH + "/InfluencePersons").child(key).delete();

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

    public void launchMap(int selected_position) {
        if (personList.get(selected_position).getLocation() != null) {
            Uri gmmIntentUri = Uri.parse("geo::0,0?q=" + personList.get(selected_position).getLocation().getLatitude()
                    + "," + personList.get(selected_position).getLocation().getLongitude() + "(" + personList.get(selected_position).getName() + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            }
        } else {
            Toast.makeText(context, "Location Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialContactPhone(final String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}
