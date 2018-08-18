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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.database.DatabaseHelper;
import sree.myparty.graph.DayAxisValueFormatter;
import sree.myparty.graph.MyAxisValueFormatter;
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


    ArrayList<String> keys;
    ArrayList<Integer> data;

    DatabaseHelper mHelper;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question;
        RadioButton option1, option2, option3;
        TextView saveAnswer;
        BarChart surveyChart;
        ImageView overFlow;



        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.surveyItem_Question);
            option1 = (RadioButton) view.findViewById(R.id.surveyItem_Option1);
            option2 = (RadioButton) view.findViewById(R.id.surveyItem_Option2);
            option3 = (RadioButton) view.findViewById(R.id.surveyItem_Option3);
            saveAnswer = (TextView) view.findViewById(R.id.survey_Save_answer);
            surveyChart = (BarChart) view.findViewById(R.id.survey_graph);
            overFlow = (ImageView)view.findViewById(R.id.overflow);
            setUpChart(surveyChart);


        }
    }


    public SurveyAdminAdapter(Context mContext, List<SurveyPojo> mSurveyList) {
        this.mContext = mContext;
        this.mSurveyList = mSurveyList;
        mSessionManager = new SessionManager(mContext);
        this.mHelper = new DatabaseHelper(mContext);

        keys = new ArrayList<>();
        data = new ArrayList<>();
    }

    @Override
    public SurveyAdminAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_item_admin, parent, false);

        return new SurveyAdminAdapter.MyViewHolder(itemView);
    }



    int selected_position = 0;
    @Override
    public void onBindViewHolder(final SurveyAdminAdapter.MyViewHolder holder, final int position) {
        final SurveyPojo survey = mSurveyList.get(position);

        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        holder.question.setTypeface(tf);
        if (survey.isActive()){
            holder.question.setText((position + 1) + ". " + survey.getSurveyQuestion());
        }
        else {
            holder.question.setText((position + 1) + ". " + survey.getSurveyQuestion()+"(Survey Ended)");

        }
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

        holder.overFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                showPopupMenu(holder.overFlow);
            }
        });
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


        if (mSurveyList.get(position).isCons()){
            holder.saveAnswer.setText("Target Area : Full Constitution");
        }
        else {
            holder.saveAnswer.setText("Target Area : Booth Number "+mSurveyList.get(position).getBoothNumber());
        }

       /* holder.saveAnswer.setOnClickListener(new View.OnClickListener() {
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
                // saveAnswer(mSurveyList.get(position).getSurveyID(),mSessionManager.getRegID(),answer);
                saveAnswer(mSurveyList.get(position).getSurveyID(), getNumber() + "", answer);

            }
        });*/

        ArrayList<String> answers = keys = mHelper.getAnswers(survey.getSurveyID());

        if (answers.size() > 0)
        {
            getOC(answers, holder.surveyChart, answers.size());
        }
        else {
            holder.surveyChart.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mSurveyList.size();
    }


    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.survey_options, popup.getMenu());
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
                case R.id.end_survey:
                    endSurvey();
                    return true;
                case R.id.delete_survey:
                    deleteSurvey();
                    return true;

                default:
            }
            return false;
        }
    }


    public void saveAnswer(String QuestionID, String userID, String answer) {
        SurveyAnswerPojo mSurveyPojo = new SurveyAnswerPojo(QuestionID, answer);
        final ProgressDialog pDialog = Constants.showDialog((Activity) mContext);
        pDialog.show();
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/SurveyAnswers").child(QuestionID).child(userID).setValue(mSurveyPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
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
public void deleteSurvey(){
    MyApplication.getFirebaseDatabase()
            .getReference(Constants.DB_PATH+"/Surveys")
            .child(mSurveyList.get(selected_position)
                    .getSurveyID()).removeValue();
    MyApplication.getFirebaseDatabase()
            .getReference(Constants.DB_PATH+"/SurveyAnswers")
            .child(mSurveyList.get(selected_position)
                    .getSurveyID()).removeValue();

}
    public void endSurvey(){
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH+"/Surveys")
                .child(mSurveyList.get(selected_position)
                        .getSurveyID())
                .child("isActive").setValue(false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Constants.showToast("Survey Ended", (Activity) mContext);

                        }
                    }
                });
    }

    public int getNumber() {
        Random random = new Random();
        return random.nextInt(9999);
    }


    public void setUpChart(BarChart mChart) {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);


        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);


        IAxisValueFormatter custom = new MyAxisValueFormatter();
        YAxis leftAxis = mChart.getAxisLeft();
    //    leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawLabels(false);// this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        mChart.notifyDataSetChanged();
    }


    private void setData(BarChart mChart, ArrayList<Integer> dataList, int keys, ArrayList<String> strings) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < dataList.size(); i++) {
            yVals1.add(new BarEntry(i, dataList.get(i), mContext.getResources().getDrawable(R.drawable.ic_girl)));
        }
        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Total Answers Polled " + keys);

            set1.setDrawIcons(false);


            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //        data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart, strings);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            xAxis.setValueFormatter(xAxisFormatter);

            mChart.setData(data);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }


    public void getOC(ArrayList<String> answers, BarChart mChart, int size) {

        keys = new ArrayList<>();
        data = new ArrayList<>();

        Set<String> unique = new HashSet<String>(answers);
        for (String key : unique) {
            data.add(Collections.frequency(answers, key));
            keys.add(key);
            //  Log.d(key ,  Collections.frequency(catageories, key)+"");
        }


        setData(mChart, data, size,keys);

    }
}

