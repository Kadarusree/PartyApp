package sree.myparty.survey;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.constuecies.Booth;
import sree.myparty.utils.Constants;

public class CreateSurvey extends AppCompatActivity {

    DatabaseReference mReference;

    String surveyID, surveyName, surveyQuestion, surveyOption1, surveyOption2, surveyOption3, surveryPostedBy;
    boolean isActive;

    @BindView(R.id.survey_name)
    EditText edt_surveyName;

    @BindView(R.id.survey_question)
    EditText edt_surveyQuestion;

    @BindView(R.id.survey_option1)
    EditText edt_surveyOption1;

    @BindView(R.id.survey_option2)
    EditText edt_surveyOption2;


    @BindView(R.id.survey_option3)
    EditText edt_surveyOption3;

    @BindView(R.id.rb_const)
    RadioButton rb_const;


    @BindView(R.id.rb_booth)
    RadioButton rb_booth;


    @BindView(R.id.booth_layout)
    LinearLayout booth_layout;

    @BindView(R.id.spin_booth)
    Spinner spin_booth;

    ProgressDialog mDialog;

    ArrayList<Booth> mBoothsList;
    ArrayList<String> boothNames;

    ArrayAdapter<String> adapter;

    boolean isConst = true;
    String boothNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);
        ButterKnife.bind(this);
        mReference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Surveys");
        mDialog = Constants.showDialog(this);
        boothNames = new ArrayList<>();
        rb_booth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_const.setChecked(false);
                    booth_layout.setVisibility(View.VISIBLE);
                    loadBooths();
                    isConst = false;
                }
            }
        });

        rb_const.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_booth.setChecked(false);
                    booth_layout.setVisibility(View.GONE);
                    isConst = true;
                }
            }
        });
    }

    public void postSurvey(View view) {
        surveyID = mReference.push().getKey();
        surveyName = edt_surveyName.getText().toString();
        surveyQuestion = edt_surveyQuestion.getText().toString();
        surveyOption1 = edt_surveyOption1.getText().toString();
        surveyOption2 = edt_surveyOption2.getText().toString();
        surveyOption3 = edt_surveyOption3.getText().toString();

        if (isConst){
            boothNumber ="";
        }
        else {
            boothNumber = mBoothsList.get(spin_booth.getSelectedItemPosition()).getBoothNumber();

        }
        if (validations()) {
            SurveyPojo mSurveyPojo = new SurveyPojo(surveyID, surveyName, surveyQuestion, surveyOption1, surveyOption2, surveyOption3, "Admin", true, isConst, boothNumber);
            save(mSurveyPojo);
        }
    }


    private boolean validations() {
        boolean isValid = false;
        if (surveyName.length() < 3) {
            edt_surveyName.setError("Enter Meeting Name");
        } else if (surveyQuestion.length() < 3) {
            edt_surveyQuestion.setError("Enter Purpose");
        } else if (surveyOption1.length() < 1) {
            edt_surveyOption1.setError("Minimum 1 Character");
        } else if (surveyOption3.length() < 1) {
            edt_surveyOption2.setError("Minimum 1 Character");
        } else if (surveyOption3.length() < 1) {
            edt_surveyOption3.setError("Minimum 1 Character");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public void save(SurveyPojo mSurveyPojo) {
        mDialog.show();

        mReference.child(mSurveyPojo.getSurveyID()).setValue(mSurveyPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Constants.showToast("Survey Posted", CreateSurvey.this);
                    edt_surveyName.setText("");
                    edt_surveyQuestion.setText("");
                    edt_surveyOption1.setText("");
                    edt_surveyOption2.setText("");
                    edt_surveyOption3.setText("");
                }

            }
        });

    }

    private void loadBooths() {
        mDialog.setMessage("Loading Booths");
        mDialog.show();
        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Booths/mBooths")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mDialog.dismiss();
                        mBoothsList = new ArrayList<>();
                        boothNames.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Booth mBooth = snapshot.getValue(Booth.class);
                            mBoothsList.add(mBooth);
                            boothNames.add(mBooth.getBoothNumber() + "-" + mBooth.getName());
                        }
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, boothNames);
                        spin_booth.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mDialog.dismiss();
                    }
                });
    }
}
