package sree.myparty.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.graph.DayAxisValueFormatter;
import sree.myparty.graph.MyAxisValueFormatter;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.utils.Constants;

public class MeetingAttendence extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private Button button;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";
    FirebaseDatabase mDatabase;


    private QRCodeReaderView qrCodeReaderView;
    ProgressDialog pDialog;


    ArrayList<Integer> count_array;
    ArrayList<String> keys_array;

    TextView lbl_meetingTitle;
    BarChart mChart;


    String meetingForReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_attendence);
        button = (Button) findViewById(R.id.button_start_scan);
        lbl_meetingTitle = findViewById(R.id.lbl_meetingTitle);
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        lbl_meetingTitle.setTypeface(tf);
        lbl_meetingTitle.setText(Constants.selected_meeting.getMeetingName());
        keys_array = new ArrayList<>();
        count_array = new ArrayList<>();



        pDialog = Constants.showDialog(this);


        if (Constants.selected_meeting.getIsForAll()){
            meetingForReference = Constants.Users_Table;
            getUsersCount();
            keys_array.add("Total Users");
            keys_array.add("Attended Users");
        }
        else {
            meetingForReference = Constants.Vol_Table;

            getUsersCount();
            keys_array.add("Volunteers");
            keys_array.add("Attended Volunteers");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrCodeReaderView.startCamera();
            }
        });
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();

/////////////Related to graph
        mChart = findViewById(R.id.attendence_cahrt);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart, keys_array);
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


    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        qrCodeReaderView.stopCamera();
        Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(500);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        submitAttendence(text);
    }


    public void submitAttendence(String key) {
        pDialog.show();
        isuserExist(key);
    }

    public void isuserExist(final String key) {
        MyApplication.getFirebaseDatabase()
                .getReference(meetingForReference).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                pDialog.dismiss();
                if (dataSnapshot.getChildrenCount() > 0) {
                    addAttendence(key);
                } else {
                    Toast.makeText(getApplicationContext(), "Not a registered User/Volunteer", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        });

    }


    public void addAttendence(String key) {
        pDialog.show();
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.Attendence_Table)
                .child(Constants.selected_meeting.getKey())
                .child(key).setValue("Present")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pDialog.dismiss();
                        if (task.isSuccessful()) {
                            getUsersCount();
                            Toast.makeText(getApplicationContext(), "Marked As Attended", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void getAttendenceCount() {
        pDialog.show();
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.Attendence_Table)
                .child(Constants.selected_meeting.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pDialog.dismiss();
                count_array.add(1, (int) dataSnapshot.getChildrenCount());
                setUpgraph(count_array, keys_array);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        });
    }

    public void getUsersCount() {
        count_array.clear();
        pDialog.show();
        MyApplication.getFirebaseDatabase()
                .getReference(meetingForReference)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pDialog.dismiss();
                        count_array.add(0, (int) dataSnapshot.getChildrenCount());
                        getAttendenceCount();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        pDialog.dismiss();
                    }
                });
    }


    public void setUpgraph(ArrayList<Integer> count_array, ArrayList<String> keys_array) {


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < count_array.size(); i++) {
            yVals1.add(new BarEntry(i, count_array.get(i), getResources().getDrawable(R.drawable.ic_girl)));
        }
        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Attendence Chart");

            set1.setDrawIcons(false);


            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //        data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            mChart.clear();
            mChart.setData(data);
            mChart.invalidate();
        }
    }
}
