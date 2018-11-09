package utthamkumar.myparty.volunteer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import utthamkumar.myparty.Adapters.NewsVolunteerAdapter;
import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.beans.NewsPojo;
import utthamkumar.myparty.misc.NewsListAdapter;
import utthamkumar.myparty.utils.Constants;
import utthamkumar.myparty.utils.MyDividerItemDecoration;

public class NewsAcceptList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<NewsPojo> newsList;
    private NewsVolunteerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        recyclerView = findViewById(R.id.recycler_view);
        newsList = new ArrayList<>();
        mAdapter = new NewsVolunteerAdapter(this, newsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
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

                    if (!mNewsItem.isAccepted()) {
                        newsList.add(mNewsItem);
                    }
                }
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();

                if (newsList.size()==0){
                    Constants.showToast("No Pending Approvals",NewsAcceptList.this);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
