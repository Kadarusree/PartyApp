package utthamkumar.myparty.survey;

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

import utthamkumar.myparty.Adapters.MeetingsAdapter;
import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.admin.MeetingsListActivity;
import utthamkumar.myparty.database.DatabaseHelper;
import utthamkumar.myparty.pojos.MeetingPojo;
import utthamkumar.myparty.session.SessionManager;
import utthamkumar.myparty.utils.Constants;

public class SurveyList extends AppCompatActivity {

    AlertDialog mDialog;
    private RecyclerView recyclerView;
    private List<SurveyPojo> mSurveyList;
    private SurveyAdapter mAdapter;

    SessionManager mSession;

    DatabaseHelper mSurveyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);
        mSurveyList = new ArrayList<>();
        mSurveyDB = new DatabaseHelper(this);

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
        //   getVolunteers();

        getAnswers();
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

                                if (!mSurveyDB.isAnswered(volItem.surveyID, mSession.getRegID())) {
                                    mSurveyList.add(volItem);
                                }

                            } else if (volItem.getBoothNumber().equalsIgnoreCase(mSession.getBoothNumber())) {
                                if (!mSurveyDB.isAnswered(volItem.surveyID, mSession.getRegID())) {
                                    mSurveyList.add(volItem);
                                }
                            }


                        }
                    }
                    if (mSurveyList.size() == 0) {
                        Toast.makeText(getApplicationContext(), "You Answered all Surveys", Toast.LENGTH_SHORT).show();
                    } else {
                        mAdapter = new SurveyAdapter(SurveyList.this, mSurveyList);
                        recyclerView.setAdapter(mAdapter);
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


    public void getAnswers() {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/SurveyAnswers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot parent : dataSnapshot.getChildren()) {
                        String surveyID = parent.getKey();
                        for (DataSnapshot child : parent.getChildren()) {
                            String userID = child.getKey();
                            mSurveyDB.insertSurveyAnswres(surveyID, userID);
                        }
                    }

                    getVolunteers();
                } else {
                    getVolunteers();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        mSurveyDB.delete();
    }
}
