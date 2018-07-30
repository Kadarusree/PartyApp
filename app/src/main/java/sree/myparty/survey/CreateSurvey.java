package sree.myparty.survey;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import sree.myparty.MyApplication;
import sree.myparty.R;
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


    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);
        ButterKnife.bind(this);
        mReference = MyApplication.getFirebaseDatabase().getReference(Constants.Survey_Table);
        mDialog = Constants.showDialog(this);
    }

    public void postSurvey(View view) {
        surveyID = mReference.push().getKey();
        surveyName = edt_surveyName.getText().toString();
        surveyQuestion = edt_surveyQuestion.getText().toString();
        surveyOption1 = edt_surveyOption1.getText().toString();
        surveyOption2 = edt_surveyOption2.getText().toString();
        surveyOption3 = edt_surveyOption3.getText().toString();

        if (validations()){
            SurveyPojo mSurveyPojo = new SurveyPojo(surveyID,surveyName,surveyQuestion,surveyOption1,surveyOption2,surveyOption3,"Admin",true);
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
        }else {
            isValid = true;
        }
        return isValid;
    }

    public void save(SurveyPojo mSurveyPojo){
        mDialog.show();

        mReference.child(mSurveyPojo.getSurveyID()).setValue(mSurveyPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Constants.showToast("Survey Posted",CreateSurvey.this);
                    edt_surveyName.setText("");
                    edt_surveyQuestion.setText("");
                    edt_surveyOption1.setText("");
                    edt_surveyOption2.setText("");
                    edt_surveyOption3.setText("");
                }

            }
        });

    }


}
