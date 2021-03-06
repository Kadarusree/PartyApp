package sree.myparty.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.List;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.admin.VolunteerLocationsList;
import sree.myparty.admin.VolunteerProfile;
import sree.myparty.pojos.ACAdminPojo;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 7/25/2018.
 */

public class AdminsListAdapter extends RecyclerView.Adapter<AdminsListAdapter.MyViewHolder> {

    private Context mContext;
    private List<ACAdminPojo> volunteerList;

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


    public AdminsListAdapter(Context mContext, List<ACAdminPojo> volunteerList) {
        this.mContext = mContext;
        this.volunteerList = volunteerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.volunteer_list_item, parent, false);

        return new MyViewHolder(itemView);
    }


    int selected_position = 0;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ACAdminPojo mVolunteer = volunteerList.get(position);
        holder.title.setText(mVolunteer.getName());

        System.out.print("");
        if (mVolunteer.isAccepted()) {
            holder.count.setText("Approved");
            holder.count.setTextColor(mContext.getResources().getColor(R.color.color_green));
        } else {
            holder.count.setText("Approval Pending");
            holder.count.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        // loading album cover using Glide library
        Glide.with(mContext).load(mVolunteer.getProfilePic()).into(holder.thumbnail);

        loadImage(mContext, holder.thumbnail, mVolunteer.getProfilePic());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.vol_dots, popup.getMenu());
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
                case R.id.action_add_favourite:

                    ACAdminPojo mVol = volunteerList.get(selected_position);
                    Constants.vAdmin  = true;
                    Intent i = new Intent(mContext, VolunteerProfile.class);
                    i.putExtra("Volunteer", (Serializable) mVol);
                    mContext.startActivity(i);
                    return true;
                case R.id.action_play_next:
                    ACAdminPojo mVol3 = volunteerList.get(selected_position);
                    dialContactPhone(mVol3.getMobileNumber());
                    return true;
                case R.id.approve:
                    ACAdminPojo mVol2 = volunteerList.get(selected_position);
                    approve(mVol2);
                    return true;
                case R.id.delete:
                    ACAdminPojo mVol4 = volunteerList.get(selected_position);
                    delete(mVol4);
                    return true;

                case R.id.track_locations:

                    Toast.makeText(mContext,"Not Available",Toast.LENGTH_SHORT).show();
                    ACAdminPojo mVol5 = volunteerList.get(selected_position);
                    Constants.admin = mVol5;
                 //   mContext.startActivity(new Intent(mContext, VolunteerLocationsList.class));
                    return true;
                default:
            }
            return false;
        }
    }

    private void delete(ACAdminPojo volunteer) {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Admins").child(volunteer.getRegID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "Failed To Delete", Toast.LENGTH_SHORT).show();

                }
            }
        });

        MyApplication.getFirebaseStorage().getReference(Constants.DB_PATH + "/Admins").child(volunteer.getRegID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  //  Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                }else {
                //   Toast.makeText(mContext, "Failed To Delete", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return volunteerList.size();
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


    public void approve(final ACAdminPojo volunteer) {






                ACAdminPojo mVolunteer = new ACAdminPojo(volunteer.getName(),
                        volunteer.getBoothnumber(),
                        volunteer.getRegID(),
                        volunteer.getPassword(),
                        volunteer.getFcmID(),
                        volunteer.getProfilePic(),
                        volunteer.getQr_URl(),
                        volunteer.getMobileNumber(),
                        true);

                MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Admins").child(volunteer.getRegID()).setValue(mVolunteer).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Sucess", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    private void dialContactPhone(final String phoneNumber) {
        mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}
