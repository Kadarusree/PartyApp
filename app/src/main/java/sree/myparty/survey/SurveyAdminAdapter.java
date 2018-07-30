package sree.myparty.survey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Random;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;

/**
 * Created by srikanthk on 7/30/2018.
 */

public class SurveyAdminAdapter extends RecyclerView.Adapter<SurveyAdminAdapter.MyViewHolder>

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


    public SurveyAdminAdapter(Context mContext, List<SurveyPojo> mSurveyList) {
        this.mContext = mContext;
        this.mSurveyList = mSurveyList;
        mSessionManager = new SessionManager(mContext);
    }

    @Override
    public SurveyAdminAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_list_item, parent, false);

        return new SurveyAdminAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SurveyAdminAdapter.MyViewHolder holder, final int position) {
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
                Constants.showToast(answer, (Activity) mContext);


                // uncomment for production
                // saveAnswer(mSurveyList.get(position).getSurveyID(),mSessionManager.getRegID(),answer);
                saveAnswer(mSurveyList.get(position).getSurveyID(), getNumber() + "", answer);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mSurveyList.size();
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.meeting_options, popup.getMenu());
        popup.setOnMenuItemClickListener(new SurveyAdminAdapter.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.attendence:
                    return true;
                case R.id.map:
                    return true;
                case R.id.delete:
                    return true;
                default:
            }
            return false;
        }
    }


    public void saveAnswer(String QuestionID, String userID, String answer) {
        final ProgressDialog pDialog = Constants.showDialog((Activity) mContext);
        pDialog.show();
        MyApplication.getFirebaseDatabase().getReference(Constants.Survey_Aswers_Table).child(QuestionID).child(userID).setValue(answer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pDialog.dismiss();
                if (task.isSuccessful()) {
                    Constants.showToast("Your Answer Posted", (Activity) mContext);

                } else {
                    Constants.showToast("Failed", (Activity) mContext);
                }
            }
        });
    }

    public int getNumber() {
        Random random = new Random();
        return random.nextInt(9999);
    }
}
