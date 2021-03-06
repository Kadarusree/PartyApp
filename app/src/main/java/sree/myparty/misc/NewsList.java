package sree.myparty.misc;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sree.myparty.DashBoard.Dashboard;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.RegistartionActivity;
import sree.myparty.beans.NewsPojo;
import sree.myparty.utils.Constants;
import sree.myparty.utils.MyDividerItemDecoration;

public class NewsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<NewsPojo> newsList;
    private NewsListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        recyclerView = findViewById(R.id.recycler_view);
        newsList = new ArrayList<>();
        mAdapter = new NewsListAdapter(this, newsList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/News").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {


                    NewsPojo mNewsItem = indi.getValue(NewsPojo.class);

                    if (mNewsItem.isAccepted()){
                        newsList.add(mNewsItem);
                    }

                }
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();

                // stop animating Shimmer and hide the layout

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /*@Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if (isTaskRoot()) {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }

    }*/
}
