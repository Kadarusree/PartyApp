package sree.myparty.survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sree.myparty.Adapters.MeetingsAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.admin.MeetingsListActivity;
import sree.myparty.pojos.MeetingPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;

public class SurveyList extends AppCompatActivity {

    AlertDialog mDialog;
    private RecyclerView recyclerView;
    private List<SurveyPojo> mSurveyList;
    private SurveyAdapter mAdapter;

    SessionManager mSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);
        mSurveyList = new ArrayList<>();

        mDialog = Constants.showDialog(this);
        mSession = new SessionManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.surveyList);

        mAdapter = new SurveyAdapter(this, mSurveyList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SurveyList.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        getVolunteers();


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

    private void getVolunteers() {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Surveys").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSurveyList.clear();

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot indi : dataSnapshot.getChildren()) {
                        SurveyPojo volItem = indi.getValue(SurveyPojo.class);
                        if (volItem.isActive()) {

                            if (volItem.isCons()) {
                                mSurveyList.add(volItem);

                            } else if (volItem.getBoothNumber().equalsIgnoreCase(mSession.getBoothNumber())) {
                                mSurveyList.add(volItem);

                            }


                        }
                    }
                    if (mSurveyList.size() < 1) {
                    } else {
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "No Active Survey Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
