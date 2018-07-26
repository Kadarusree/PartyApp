package sree.myparty.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.R;
import sree.myparty.pojos.LatLng;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        ButterKnife.bind(this);
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
}
