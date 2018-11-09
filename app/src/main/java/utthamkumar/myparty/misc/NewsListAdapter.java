package utthamkumar.myparty.misc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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


import utthamkumar.myparty.R;
import utthamkumar.myparty.beans.NewsPojo;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> {
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
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        holder.title.setTypeface(tf);
        holder.title.setText(recipe.getTitle());

        holder.postedby.setText("Posted by : " + recipe.getPostedby()
                +"  On : "+getDate(Long.parseLong(recipe.getTimestamp()), "dd/MM/yyyy HH:mm aa"));
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
                        +"\n\n"+cartList.get(position).getDescription()
                        +"\n\n"+cartList.get(position).getImageUrl());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
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
}
