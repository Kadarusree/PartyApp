package sree.myparty.survey;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.database.DatabaseHelper;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

public class SurveyAdminList extends AppCompatActivity {

    AlertDialog mDialog;
    private RecyclerView recyclerView;
    private List<SurveyPojo> mSurveyList;
    private SurveyAdminAdapter mAdapter;
    ArrayList<SurveyAnswerPojo> mSurveyAnswersList;


    DatabaseHelper mSurveyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_admin_list);
        ButterKnife.bind(this);
        mSurveyList = new ArrayList<>();

        mDialog = Constants.showDialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.surveyList);

        mAdapter = new SurveyAdminAdapter(this, mSurveyList);

        mSurveyDB = new DatabaseHelper(this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SurveyAdminList.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getVolunteers();


    }

    @OnClick(R.id.btn_createSurvey)
    public void createSurvey(View view) {
        ActivityLauncher.launchCreateSurvey(this);
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
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Surveys").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSurveyList.clear();

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot indi : dataSnapshot.getChildren()) {
                        SurveyPojo volItem = indi.getValue(SurveyPojo.class);
                        mSurveyList.add(volItem);
                    }
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    getAnswers();
                } else {
                    Toast.makeText(getApplicationContext(), "No Survey Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getAnswers() {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/SurveyAnswers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSurveyAnswersList = new ArrayList<>();

                if (dataSnapshot.getChildrenCount() > 0) {
                    Log.d("Parent", dataSnapshot.getChildrenCount() + "");
                    for (DataSnapshot indi : dataSnapshot.getChildren()) {

                        for (DataSnapshot Child : indi.getChildren()) {
                            Log.d("Child", indi.getChildrenCount() + "");
                            SurveyAnswerPojo mSurvey = Child.getValue(SurveyAnswerPojo.class);
                            mSurveyAnswersList.add(mSurvey);
                        }

                    }
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mSurveyDB.delete();
                    mSurveyDB.insertAnswers(mSurveyAnswersList);


                } else {
                    //       Toast.makeText(getApplicationContext(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
      //  mSurveyDB.delete();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
