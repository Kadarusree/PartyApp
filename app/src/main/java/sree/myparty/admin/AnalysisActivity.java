package sree.myparty.admin;

import android.app.ProgressDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.database.DatabaseHelper;
import sree.myparty.graph.DayAxisValueFormatter;
import sree.myparty.graph.MyAxisValueFormatter;
import sree.myparty.graph.SubAxisForamtter;
import sree.myparty.pojos.VoterPojo;
import sree.myparty.utils.Constants;

public class AnalysisActivity extends AppCompatActivity implements OnChartValueSelectedListener {


    ArrayList<VoterPojo> mVotersList;
    private DatabaseHelper db;
    ProgressDialog pDialog;


    protected BarChart mChart;
    protected BarChart subChart;

    ArrayList<Integer> data;
    ArrayList<String> keys;

    ArrayList<Integer> casteData;
    ArrayList<String> casteNames = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        db = new DatabaseHelper(this);
        pDialog = Constants.showDialog(this);
        pDialog.show();

        data = new ArrayList<>();
        keys = new ArrayList<>();



        casteData = new ArrayList<>();
        casteNames = new ArrayList<>();





        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Voters").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mVotersList = new ArrayList<>();
                pDialog.dismiss();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    VoterPojo mPojo = indi.getValue(VoterPojo.class);
                    mVotersList.add(mPojo);
                }
                long records = db.insertVoters(mVotersList);
                if (records > 0) {
                    ArrayList<String> catageories = db.getCatageories();
                    getOC(catageories);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        });
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

      //  getCasteData(keys.get((int) h.getX()) + "");

    }


    @Override
    public void onNothingSelected() {

    }


    private void setData(ArrayList<Integer> dataList, ArrayList<String> keys) {
        mChart = findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart, keys);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

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
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);



        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < dataList.size(); i++) {
            yVals1.add(new BarEntry(i, dataList.get(i), getResources().getDrawable(R.drawable.ic_girl)));
        }
        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1,"");

            set1.setDrawIcons(false);


            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //        data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }


    public void getOC(ArrayList<String> catageories) {

        data.clear();
        keys.clear();

        Set<String> unique = new HashSet<String>(catageories);
        for (String key : unique) {
            data.add(Collections.frequency(catageories, key));
            keys.add(key);
            //  Log.d(key ,  Collections.frequency(catageories, key)+"");
        }


        setData(data, keys);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.delete();
    }


    private void getCasteData(String s) {
    ArrayList<String>  d  = db.getCasteData(s);
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

        IAxisValueFormatter xAxisFormatter = new SubAxisForamtter(subChart, keys2);

        XAxis xAxis = subChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //1     xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //2       leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = subChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//3        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = subChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        subChart.notifyDataSetChanged();

        setupSubchartData(subChart,subData,keys2);
    }

    private void setupSubchartData(BarChart subChart, ArrayList<Integer> data, ArrayList<String> keys) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        for (int i = 0; i < data.size(); i++) {

            yVals1.add(new BarEntry(i, data.get(i), getResources().getDrawable(R.drawable.ic_girl)));
        }
        BarDataSet set1;

        if (subChart.getData() != null &&
                subChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            subChart.getData().notifyDataChanged();
            subChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);



            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data2 = new BarData(dataSets);
            data2.setValueTextSize(10f);
            //        data.setValueTypeface(mTfLight);
            data2.setBarWidth(0.9f);

            subChart.setData(data2);
        }
    }
}