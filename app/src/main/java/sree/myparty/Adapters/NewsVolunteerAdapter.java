package sree.myparty.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.beans.NewsPojo;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 8/14/2018.
 */

public class NewsVolunteerAdapter extends RecyclerView.Adapter<NewsVolunteerAdapter.MyViewHolder> {
    private Context context;
    private List<NewsPojo> cartList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, postedby;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.news_title);
            description = view.findViewById(R.id.news_description);
            postedby = view.findViewById(R.id.news_by_dateTime);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
        }
    }


    public NewsVolunteerAdapter(Context context, List<NewsPojo> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.volunteer_news_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    int selected_position = 0;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NewsPojo recipe = cartList.get(position);
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        holder.title.setTypeface(tf);
        holder.title.setText(recipe.getTitle());

        holder.postedby.setText("Posted by : " + recipe.getPostedby()
                + "  On : " + getDate(Long.parseLong(recipe.getTimestamp()), "dd/MM/yyyy HH:mm aa"));
        holder.description.setText(recipe.getDescription());
        Glide.with(context)
                .load(recipe.getImageUrl())
                .into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, cartList.get(position).getTitle()
                        + "\n\n" + cartList.get(position).getDescription()
                        + "\n\n" + cartList.get(position).getImageUrl());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

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
        return cartList.size();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.volunteer_news_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.approve:
                    NewsPojo mPojo = cartList.get(selected_position);
                    approve(mPojo);
                    return true;
                case R.id.edit:
                    Toast.makeText(context, "Delete and post a new News", Toast.LENGTH_LONG).show();

                    return true;
                case R.id.delete:
                    NewsPojo mPojo2 = cartList.get(selected_position);
                    deleteNews(mPojo2);
                    return true;

                default:
            }
            return false;
        }
    }

    public void approve(NewsPojo mNews) {
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/News")
                .child(mNews.getId()).child("accepted").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Approved", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void deleteNews(NewsPojo mNews) {
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/News")
                .child(mNews.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to Delete", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
