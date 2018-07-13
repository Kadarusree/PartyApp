package sree.myparty.misc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


import sree.myparty.R;
import sree.myparty.beans.NewsPojo;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {
    private Context context;
    private List<NewsPojo> cartList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price, chef, timestamp;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            chef = view.findViewById(R.id.chef);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public NewsListAdapter(Context context, List<NewsPojo> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NewsPojo recipe = cartList.get(position);
        holder.name.setText(recipe.getTitle());
        holder.chef.setText("Posted by : " + recipe.getPostedby());
        holder.description.setText(recipe.getDescription());
        holder.price.setVisibility(View.GONE);
       holder.timestamp.setText(getDate(Long.parseLong(recipe.getTimestamp()),"dd/MM/yyyy HH:mm aa"));

        Glide.with(context)
                .load(recipe.getImageUrl())
                .into(holder.thumbnail);
    }
    // recipe
    @Override
    public int getItemCount() {
        return cartList.size();
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
