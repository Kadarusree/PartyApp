package sree.myparty.survey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sree.myparty.Adapters.MeetingsAdapter;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.admin.MeetingAttendence;
import sree.myparty.graph.DayAxisValueFormatter;
import sree.myparty.graph.MyAxisValueFormatter;
import sree.myparty.pojos.MeetingPojo;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 7/30/2018.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.MyViewHolder>

{

    private Context mContext;
    private List<SurveyPojo> mSurveyList;
    SessionManager mSessionManager;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question;
        RadioButton option1, option2, option3;
        Button saveAnswer;


        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.surveyItem_Question);
            option1 = (RadioButton) view.findViewById(R.id.surveyItem_Option1);
            option2 = (RadioButton) view.findViewById(R.id.surveyItem_Option2);
            option3 = (RadioButton) view.findViewById(R.id.surveyItem_Option3);
            saveAnswer = (Button) view.findViewById(R.id.survey_Save_answer);

        }
    }


    public SurveyAdapter(Context mContext, List<SurveyPojo> mSurveyList) {
        this.mContext = mContext;
        this.mSurveyList = mSurveyList;
        mSessionManager = new SessionManager(mContext);
    }

    @Override
    public SurveyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_list_item, parent, false);

        return new SurveyAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SurveyAdapter.MyViewHolder holder, final int position) {
        final SurveyPojo survey = mSurveyList.get(position);

        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.question.setTypeface(tf);
        holder.question.setText((position + 1) + ". " + survey.getSurveyQuestion());
        holder.option1.setText(survey.getSurveyOption1());
        holder.option2.setText(survey.getSurveyOption2());
        holder.option3.setText(survey.getSurveyOption3());


        if (survey.isOption1Selected()) {
            holder.option1.setChecked(true);
        } else if (survey.isOption2Selected()) {
            holder.option2.setChecked(true);
        } else if (survey.isOption3Selected()) {
            holder.option3.setChecked(true);
        }

        holder.option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    holder.option2.setChecked(false);
                    holder.option3.setChecked(false);
                    mSurveyList.get(position).setOption1Selected(true);
                    mSurveyList.get(position).setOption2Selected(false);
                    mSurveyList.get(position).setOption3Selected(false);

                }

            }
        });
        holder.option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    holder.option1.setChecked(false);
                    holder.option3.setChecked(false);
                    mSurveyList.get(position).setOption1Selected(false);
                    mSurveyList.get(position).setOption2Selected(true);
                    mSurveyList.get(position).setOption3Selected(false);
                }

            }
        });
        holder.option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    holder.option1.setChecked(false);
                    holder.option2.setChecked(false);
                    mSurveyList.get(position).setOption1Selected(false);
                    mSurveyList.get(position).setOption2Selected(false);
                    mSurveyList.get(position).setOption3Selected(true);
                }

            }
        });


        holder.saveAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "";
                if (mSurveyList.get(position).isOption1Selected()) {
                    answer = mSurveyList.get(position).getSurveyOption1();
                } else if (mSurveyList.get(position).isOption2Selected()) {
                    answer = mSurveyList.get(position).getSurveyOption2();
                } else if (mSurveyList.get(position).isOption3Selected()) {
                    answer = mSurveyList.get(position).getSurveyOption3();
                }

                // uncomment for production
                saveAnswer(mSurveyList.get(position).getSurveyID(), mSessionManager.getRegID(), answer);
                //   saveAnswer(mSurveyList.get(position).getSurveyID(), getNumber() + "", answer);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mSurveyList.size();
    }





    public void saveAnswer(final String QuestionID, final String userID, String answer) {
        final SurveyAnswerPojo mSurveyPojo = new SurveyAnswerPojo(QuestionID, answer);
        final ProgressDialog pDialog = Constants.showDialog((Activity) mContext);
        pDialog.show();

        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/SurveyAnswers").child(QuestionID).child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pDialog.dismiss();

                if (dataSnapshot.getValue() == null) {
                    MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/SurveyAnswers").child(QuestionID).child(userID).setValue(mSurveyPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pDialog.dismiss();
                            if (task.isSuccessful()) {
                                increasePoints(mSessionManager.getRegID());
                                Constants.showToast("Your Answer Posted, You earned 5 Points ", (Activity) mContext);

                            } else {
                                Constants.showToast("Failed", (Activity) mContext);
                            }
                        }
                    });

                } else {
                    Constants.showToast("You Already Answered This Survey", (Activity) mContext);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public int getNumber() {
        Random random = new Random();
        return random.nextInt(9999);
    }

    public void increasePoints(final String regID) {


        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Users")
                .child(regID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetailPojo mUser = null;
                if (dataSnapshot.getChildrenCount() > 0) {

                    mUser = dataSnapshot.getValue(UserDetailPojo.class);

                }

                int currentPoints = mUser.getPoints();
                int newpoints = currentPoints + 5;

                MyApplication.getFirebaseDatabase()
                        .getReference(Constants.DB_PATH + "/Users")
                        .child(regID).child("points").setValue(newpoints);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
