package utthamkumar.myparty.admin;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.database.DatabaseHelper;
import utthamkumar.myparty.graph.DayAxisValueFormatter;
import utthamkumar.myparty.graph.MyAxisValueFormatter;
import utthamkumar.myparty.graph.SubAxisForamtter;
import utthamkumar.myparty.pojos.VoterPojo;
import utthamkumar.myparty.utils.Constants;

public class BoothWiseVotesAnalysis extends AppCompatActivity {


    ArrayList<VoterPojo> mVotersList;
    private DatabaseHelper db;
    ProgressDialog pDialog;


    protected BarChart mChart;
    protected BarChart subChart;

    ArrayList<Integer> data;
    ArrayList<String> keys_;


    ArrayList<Integer> next_data;
    ArrayList<String> next_keys_;

    ArrayList<Integer> casteData;
    ArrayList<String> casteNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        getSupportActionBar().setTitle("Present Trend in Booth : "+Constants.selected_booth_id);
        db = new DatabaseHelper(this);
        pDialog = Constants.showDialog(this);
        pDialog.show();

        data = new ArrayList<>();
        keys_ = new ArrayList<>();

        next_data = new ArrayList<>();
        next_keys_ = new ArrayList<>();


        casteData = new ArrayList<>();
        casteNames = new ArrayList<>();

        mChart = findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //   getCasteData(keys.get((int) h.getX()) + "");
            }

            @Override
            public void onNothingSelected() {

            }
        });
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);


        IAxisValueFormatter custom = new MyAxisValueFormatter();
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
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


        /////////////Chart2////////////////////
        subChart = findViewById(R.id.chart2);
        subChart.setDrawBarShadow(false);
        subChart.setDrawValueAboveBar(true);
        subChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        subChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        subChart.setPinchZoom(false);

        subChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);



        IAxisValueFormatter custom2 = new MyAxisValueFormatter();

        YAxis leftAxis2 = subChart.getAxisLeft();
        //2       leftAxis.setTypeface(mTfLight);
        leftAxis2.setLabelCount(8, false);
        leftAxis2.setValueFormatter(custom2);
        leftAxis2.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis2.setSpaceTop(15f);
        leftAxis2.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis2 = subChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//3        rightAxis.setTypeface(mTfLight);
        rightAxis2.setLabelCount(8, false);
        rightAxis2.setValueFormatter(custom2);
        rightAxis2.setSpaceTop(15f);
        rightAxis2.setAxisMinimum(0f);
        rightAxis2.setDrawLabels(false);// this replaces setStartAtZero(true)

        Legend l2 = subChart.getLegend();
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l2.setDrawInside(false);
        l2.setForm(Legend.LegendForm.SQUARE);
        l2.setFormSize(9f);
        l2.setTextSize(11f);
        l2.setXEntrySpace(4f);


        subChart.notifyDataSetChanged();


        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Voters")
                .orderByChild("boothNumber")
                .equalTo(Constants.selected_booth_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mVotersList = new ArrayList<>();
                pDialog.dismiss();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    VoterPojo mPojo = indi.getValue(VoterPojo.class);
                    mVotersList.add(mPojo);
                }
                long records = db.insertlastotes(mVotersList);
                long records2 = db.insertfuturevotes(mVotersList);

                if (records > 0) {
                    ArrayList<String> catageories = db.getLastVotes();
                    getOC(catageories, 1);
                }

                if (records2 > 0) {
                    ArrayList<String> catageories = db.getNextVotes();
                    getNextOC(catageories, 2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        });
    }


    private void setData(ArrayList<Integer> dataList, ArrayList<String> keys) {
        ArrayList<Integer> colors = new ArrayList<>();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < dataList.size(); i++) {
            yVals1.add(new BarEntry(i, dataList.get(i), getResources().getDrawable(R.drawable.ic_girl)));
            if (keys.get(i).equalsIgnoreCase("TDP")) {
                colors.add(Color.parseColor("#FFFF00"));

            } else if (keys.get(i).equalsIgnoreCase("TRS")) {
                colors.add(Color.parseColor("#D94E8E"));

            } else if (keys.get(i).equalsIgnoreCase("BJP")) {
                colors.add(Color.parseColor("#FF671E"));
            } else if (keys.get(i).equalsIgnoreCase("Congress")) {
                colors.add(Color.parseColor("#008924"));
            }
            else if (keys.get(i).equalsIgnoreCase("BSP")) {
                colors.add(Color.parseColor("#2D55AC"));
            }
            else if (keys.get(i).equalsIgnoreCase("SP")) {
                colors.add(Color.parseColor("#dc1627"));
            }
            else if (keys.get(i).equalsIgnoreCase("AAP")) {
                colors.add(Color.parseColor("#2D55AC"));
            }
            else if (keys.get(i).equalsIgnoreCase("Others")) {
                colors.add(Color.parseColor("#BDBDBD"));
            }
            else if (keys.get(i).equalsIgnoreCase("RLD")) {
                colors.add(Color.parseColor("#AFDF3D"));
            }
            else  {
                colors.add(Color.parseColor("#BDBDBD"));
            }
        }
        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Last Election Votes for each party");

            set1.setDrawIcons(false);
            set1.setColors(colors);


            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //        data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);



            IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart, keys);
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


    public void getOC(ArrayList<String> catageories, int i) {

        data.clear();
        keys_.clear();

        Set<String> unique = new HashSet<String>(catageories);
        for (String key : unique) {
            data.add(Collections.frequency(catageories, key));
            keys_.add(key);
            //  Log.d(key ,  Collections.frequency(catageories, key)+"");
        }


        if (i == 1) {
            setData(data, keys_);
        } else if (i == 2) {
            //   setFutureVotesData(data, keys_);

        }
    }

    public void getNextOC(ArrayList<String> catageories, int i) {

        next_data.clear();
        next_keys_.clear();

        Set<String> unique = new HashSet<String>(catageories);
        for (String key : unique) {
            next_data.add(Collections.frequency(catageories, key));
            next_keys_.add(key);
            //  Log.d(key ,  Collections.frequency(catageories, key)+"");
        }

        setFutureVotesData(next_data, next_keys_);


    }


    @Override
    protected void onPause() {
        super.onPause();
        db.delete();
    }

    private void getCasteData(String s) {
        ArrayList<String> d = db.getCasteData(s);
        System.out.println(d.size());
        CastesCount(d);
    }

    public void CastesCount(ArrayList<String> catageories) {
        casteData.clear();
        casteNames.clear();

        Set<String> unique = new HashSet<String>(catageories);
        for (String key : unique) {
            casteData.add(Collections.frequency(catageories, key));
            casteNames.add(key);
            //  Log.d(key ,  Collections.frequency(catageories, key)+"");
        }


        prepareSubchart(casteData, casteNames);


    }


    private void prepareSubchart(ArrayList<Integer> subData, ArrayList<String> keys2) {
        //setupSubchartData(subChart, subData, keys2);
    }

   /* private void setupSubchartData(BarChart subChart, ArrayList<Integer> subdata, ArrayList<String> keys) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        subChart.clear();
        for (int i = 0; i < subdata.size(); i++) {

            yVals1.add(new BarEntry(i, subdata.get(i), getResources().getDrawable(R.drawable.ic_girl)));
        }
        BarDataSet set1;

        if (subChart.getData() != null &&
                subChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) subChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            subChart.getData().notifyDataChanged();
            subChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Voters in Constituency");

            set1.setDrawIcons(false);



            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data2 = new BarData(dataSets);
            data2.setValueTextSize(10f);
            //        data.setValueTypeface(mTfLight);
            data2.setBarWidth(0.9f);

            subChart.setData(data2);
            subChart.notifyDataSetChanged();
            subChart.invalidate();
        }
    }*/


    private void setFutureVotesData(ArrayList<Integer> dataList, ArrayList<String> keys) {


        ArrayList<Integer> colors = new ArrayList<>();

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < dataList.size(); i++) {

            yVals1.add(new BarEntry(i, dataList.get(i), getResources().getDrawable(R.drawable.ic_girl)));

            if (keys.get(i).equalsIgnoreCase("TDP")) {
                colors.add(Color.parseColor("#FFFF00"));

            } else if (keys.get(i).equalsIgnoreCase("TRS")) {
                colors.add(Color.parseColor("#D94E8E"));

            } else if (keys.get(i).equalsIgnoreCase("BJP")) {
                colors.add(Color.parseColor("#FF671E"));
            } else if (keys.get(i).equalsIgnoreCase("Congress")) {
                colors.add(Color.parseColor("#008924"));
            }
            else if (keys.get(i).equalsIgnoreCase("BSP")) {
                colors.add(Color.parseColor("#2D55AC"));
            }
            else if (keys.get(i).equalsIgnoreCase("SP")) {
                colors.add(Color.parseColor("#dc1627"));
            }
            else if (keys.get(i).equalsIgnoreCase("AAP")) {
                colors.add(Color.parseColor("#2D55AC"));
            }
            else if (keys.get(i).equalsIgnoreCase("Others")) {
                colors.add(Color.parseColor("#BDBDBD"));
            }
            else if (keys.get(i).equalsIgnoreCase("RLD")) {
                colors.add(Color.parseColor("#AFDF3D"));
            }
            else  {
                colors.add(Color.parseColor("#BDBDBD"));
            }
        }
        BarDataSet set1;

        IAxisValueFormatter xAxisFormatter2 = new SubAxisForamtter(subChart, keys);

        XAxis xAxis2 = subChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        //1     xAxis.setTypeface(mTfLight);
        xAxis2.setDrawGridLines(false);
        xAxis2.setGranularity(1f); // only intervals of 1 day
        xAxis2.setLabelCount(7);
        xAxis2.setValueFormatter(xAxisFormatter2);

        if (subChart.getData() != null &&
                subChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) subChart.getData().getDataSetByIndex(0);
            subChart.setDrawBarShadow(true);
            set1.setValues(yVals1);
            subChart.getData().notifyDataChanged();
            subChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Next Election Estimated Votes for each party");
            set1.setColors(colors);

            set1.setDrawIcons(false);


            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //        data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            subChart.setData(data);
            subChart.notifyDataSetChanged();
            subChart.invalidate();
        }
    }

}
