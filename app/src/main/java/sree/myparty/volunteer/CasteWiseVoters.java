package sree.myparty.volunteer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Config;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.adapters.CasteWiseVotersAdapter;
import sree.myparty.pojos.CasteWiseVoterBean;
import sree.myparty.pojos.InfluPerson;
import sree.myparty.pojos.VoterPojo;
import sree.myparty.utils.Constants;
import sree.myparty.utils.MyDividerItemDecoration;
import sree.myparty.utils.VolunteerSessionManager;

public class CasteWiseVoters extends AppCompatActivity {


    @BindView(R.id.cwv_name)
    EditText edt_name;

    @BindView(R.id.cwv_voterID)
    EditText edt_voterID;

    @BindView(R.id.cwv_father_name)
    EditText edt_fatherName;

    @BindView(R.id.cwv_age)
    EditText edt_age;

    @BindView(R.id.cwv_mobile_number)
    EditText edt_mobile_number;

    @BindView(R.id.cwv_address)
    EditText edt_address;

    @BindView(R.id.cwv_catageory)
    Spinner spn_catageory;

    @BindView(R.id.cwv_casteName)
    EditText edt_casteName;

    @BindView(R.id.cwv_boothnum)
    EditText edt_BoothNum;

    @BindView(R.id.cwv_location)
    EditText edt_location;

    @BindView(R.id.cwv_rb_male)
    RadioButton rb_male;

    @BindView(R.id.cwv_rb_female)
    RadioButton rb_female;

    DatabaseReference mReference;

    AlertDialog mDialog;

    private RecyclerView recyclerView;
    private List<CasteWiseVoterBean> newsList;
    private CasteWiseVotersAdapter mAdapter;

    int PLACE_PICKER_REQUEST = 1;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    LatLng location = null;
    String voterID, name, fatherName, sex, age, mobileNumber, caste, boothNumber, catageory, address;

    private FusedLocationProviderClient mFusedLocationClient;

    LatLng addedLocation;

    VolunteerSessionManager mVolunteerSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caste_wise_voters);
        ButterKnife.bind(this);
        mReference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Voters");
        mDialog = Constants.showDialog(this);

   mVolunteerSessionManager = new VolunteerSessionManager(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                getLocation();
            }
        } else {
            getLocation();
        }

        /*recyclerView = findViewById(R.id.list_castewiseVoters);
        newsList = new ArrayList<>();
        mAdapter = new CasteWiseVotersAdapter(this, newsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);*/
        // mDialog.show();

        rb_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    sex = rb_female.getText().toString();
                    rb_male.setChecked(false);

                }

            }
        });
        rb_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    sex = rb_male.getText().toString();
                    rb_female.setChecked(false);

                }

            }
        });
        spn_catageory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*mReference.
                        child(getResources().
                                getStringArray(R.array.catageories)[edt_caste.getSelectedItemPosition()]).
                        addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mDialog.dismiss();
                        newsList.clear();
                        for (DataSnapshot indi : dataSnapshot.getChildren()) {
                            CasteWiseVoterBean mNewsItem = indi.getValue(CasteWiseVoterBean.class);
                            newsList.add(mNewsItem);
                        }
                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

                        // stop animating Shimmer and hide the layout

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mDialog.dismiss();
                    }
                });*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @OnClick(R.id.btn_cwv_save)
    public void onButtonClick(View v) {


        voterID = edt_voterID.getText().toString();
        name = edt_name.getText().toString();
        fatherName = edt_name.getText().toString();
        catageory = getResources().getStringArray(R.array.catageories)[spn_catageory.getSelectedItemPosition()];
        boothNumber = edt_BoothNum.getText().toString().trim();
        caste = edt_casteName.getText().toString().trim();
        age = edt_age.getText().toString().trim();
        address = edt_address.getText().toString().trim();
        mobileNumber = edt_mobile_number.getText().toString();

        if (validations()) {
            VoterPojo mVoter = new VoterPojo(voterID, name, fatherName, sex
                    , Integer.parseInt(age), mobileNumber, address, catageory, caste, boothNumber, location, mVolunteerSessionManager.getVolName(), addedLocation);

            save(mVoter);
//            Snackbar.make(null,"Validations are good",Snackbar.LENGTH_SHORT).show();
        }
        /*CasteWiseVoterBean mVoter = new CasteWiseVoterBean(name, voterID, caste, System.currentTimeMillis() + "", Constants.VOLUNTEER, boothNum);
        save(mVoter);*/
    }

    @OnClick(R.id.pick_loaction)
    public void onClick(View v) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void save(VoterPojo mVoter) {
        String key = mReference.push().getKey();
        mDialog.show();
        mReference.child(boothNumber).child(key).setValue(mVoter).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Constants.showToast("Added SucessFully", CasteWiseVoters.this);
                    edt_name.clearComposingText();
                    edt_voterID.clearComposingText();
                    edt_fatherName.clearComposingText();
                    edt_age.clearComposingText();
                    edt_address.clearComposingText();
                    edt_casteName.clearComposingText();
                    edt_BoothNum.clearComposingText();
                    edt_location.clearComposingText();
                    rb_female.setChecked(false);
                    rb_male.setChecked(false);
                } else {
                    Constants.showToast("Failed To add", CasteWiseVoters.this);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                location = place.getLatLng();
                edt_location.setText(String.format("%s", place.getName()));

               /* String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    }

                } else {

                }
                return;
            }

        }
    }

    private boolean validations() {
        boolean isValid = false;
        if (voterID.length() < 3) {
            edt_voterID.setError("Enter Voter ID");
        } else if (name.length() < 3) {
            edt_name.setError("Enter valid name");
        } else if (fatherName.length() < 3) {
            edt_fatherName.setError("Enter valid name");
        } else if (age.length() == 0 || Integer.parseInt(age) < 0 || Integer.parseInt(age) > 110) {
            edt_age.setError("Enter valid age");
        } else if (mobileNumber.length() != 10) {
            edt_mobile_number.setError("Enter 10 digit mobile number");
        } else if (address.length() < 3) {
            edt_address.setError("Enter valid adress");
        } else if (caste.length() < 3) {
            edt_casteName.setError("Enter Caste");
        } else if (boothNumber.length() < 1) {
            edt_BoothNum.setError("Enter Booth number");
        } else if (location == null) {
            edt_location.setError("Pick Home Location");
        } else if (!rb_female.isChecked() && !rb_male.isChecked()) {
           Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
        } else {
            isValid = true;
        }

        return isValid;
    }


    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            addedLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        }
                    }
                });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("You need us to use your location to work with this module")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(CasteWiseVoters.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


}
