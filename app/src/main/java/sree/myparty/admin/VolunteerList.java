package sree.myparty.admin;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import sree.myparty.Adapters.InfluencePersonAdapter;
import sree.myparty.Adapters.VolunteersListAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.Album;
import sree.myparty.pojos.InfluPerson;
import sree.myparty.pojos.VolunteerPojo;
import sree.myparty.pojos.VoterPojo;
import sree.myparty.utils.Constants;
import sree.myparty.utils.MyDividerItemDecoration;

public class VolunteerList extends AppCompatActivity {

    DatabaseReference mDbref;

    AlertDialog mDialog;

    private RecyclerView recyclerView;
    private List<VolunteerPojo> volunteerList;
    private VolunteersListAdapter mAdapter;

    private List<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_list);
        ButterKnife.bind(this);
        volunteerList = new ArrayList<>();

        mDialog = Constants.showDialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.list_volunteers);

        albumList = new ArrayList<>();
        mAdapter = new VolunteersListAdapter(this, volunteerList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getVolunteers();


    }

    private void getVolunteers(){
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/Volunteers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                volunteerList.clear();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    VolunteerPojo volItem = indi.getValue(VolunteerPojo.class);
                    volunteerList.add(volItem);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar,
                R.drawable.avatar};

        Album a = new Album("True Romance", 13, covers[0]);
        albumList.add(a);

        a = new Album("Xscpae", 8, covers[1]);
        albumList.add(a);

        a = new Album("Maroon 5", 11, covers[2]);
        albumList.add(a);

        a = new Album("Born to Die", 12, covers[3]);
        albumList.add(a);

        a = new Album("Honeymoon", 14, covers[4]);
        albumList.add(a);

        a = new Album("I Need a Doctor", 1, covers[5]);
        albumList.add(a);

        a = new Album("Loud", 11, covers[6]);
        albumList.add(a);

        a = new Album("Legend", 14, covers[7]);
        albumList.add(a);

        a = new Album("Hello", 11, covers[8]);
        albumList.add(a);

        a = new Album("Greatest Hits", 17, covers[9]);
        albumList.add(a);


    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
