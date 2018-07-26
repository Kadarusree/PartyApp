package sree.myparty.admin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.LatLng;
import sree.myparty.pojos.MeetingPojo;
import sree.myparty.utils.Constants;

public class MeetingsActivity extends AppCompatActivity {


    @BindView(R.id.meeting_name)
    EditText meetingName;

    @BindView(R.id.meeting_purpose)
    EditText meetingPurpose;

    @BindView(R.id.meeting_date_time)
    EditText meetingDateTime;

    @BindView(R.id.meeting_location)
    EditText meetingLocation;

    @BindView(R.id.meeting_vol)
    RadioButton meetingVolunteer;


    @BindView(R.id.meeting_all)
    RadioButton meeting_all;



    String name, purpose, dateTime, location_name;

    boolean is_for_all;


    int PLACE_PICKER_REQUEST = 1;
    LatLng location = null;


    ProgressDialog pDailog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        ButterKnife.bind(this);
        pDailog = Constants.showDialog(this);

        meetingDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDateTimePicker();
                }
            }
        });

        meetingDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        meetingVolunteer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    is_for_all = false;
                    meeting_all.setChecked(false);

                }
            }
        });
        meeting_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    is_for_all = true;
                    meetingVolunteer.setChecked(false);

                }
            }
        });
    }


    @OnClick(R.id.meeting_loaction_pick)
    public void onLocationClick(View v){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                location = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);
                meetingLocation.setText(String.format("%s", place.getName()));

               /* String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
            }
        }
    }

    private boolean validations() {
        boolean isValid = false;
        if (name.length() < 3) {
            meetingName.setError("Enter Meeting Name");
        } else if (purpose.length() < 3) {
            meetingPurpose.setError("Enter Purpose");
        } else if (dateTime.length() < 3) {
            meetingDateTime.setError("Selet Date Time");
        } else if (location == null) {
            meetingLocation.setError("Pick Venue");
        } else if (!meetingVolunteer.isChecked() && !meeting_all.isChecked()) {
            Toast.makeText(getApplicationContext(), "Select Meeting For", Toast.LENGTH_SHORT).show();
        } else {
            isValid = true;
        }

        return isValid;
    }


    @OnClick(R.id.btn_save_meeting)
    public void shcehdle(View view) {

        name = meetingName.getText().toString();
        purpose = meetingPurpose.getText().toString();
        dateTime = meetingDateTime.getText().toString();
        location_name = meetingLocation.getText().toString();
        if (validations()){
               MeetingPojo mPojo = new MeetingPojo(name, purpose,dateTime,location_name,is_for_all,"Admin",location);
               save(mPojo);
        }
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
      final Calendar date = Calendar.getInstance();
        new DatePickerDialog(MeetingsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(MeetingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v("TAG", "The choosen one " + date.getTime());
                        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:MM");
                        meetingDateTime.setText(format1.format(date.getTime()));
                      //  meetingDateTime.setEnabled(false);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }


    public void save(MeetingPojo mPojo){

       DatabaseReference mRef =  MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/Meetings");
       String key = mRef.push().getKey();
       pDailog.show();
       mRef.child(key).setValue(mPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               pDailog.dismiss();
               if (task.isSuccessful()){
                   Constants.showToast("Meeting Scheduled",MeetingsActivity.this);

                   location = null;
                   meetingName.setText("");
                   meetingPurpose.setText("");
                   meetingDateTime.setText("");
                   meetingLocation.setText("");
                   meetingVolunteer.setChecked(false);
                   meeting_all.setChecked(false);
               }

           }
       });

    }
}
