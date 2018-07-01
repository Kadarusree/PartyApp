package sree.myparty;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sree.myparty.apis.ApiClient;
import sree.myparty.apis.ApiInterface;
import sree.myparty.firebase.Data;
import sree.myparty.firebase.FirebasePushModel;
import sree.myparty.utils.Constants;

public class RegistartionActivity extends AppCompatActivity {


    @BindView(R.id.tv_laningText)
    TextView mLandigText;

    @BindView(R.id.edt_voterID)
    EditText mVoterID;

    @BindView(R.id.edt_mobilenumber)
    EditText mMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registartion);
        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0\n Hyderabad Constituency");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);

        FirebaseInstanceId.getInstance().getToken();

        ApiInterface apiService =
                ApiClient.getFirebaseClient().create(ApiInterface.class);
        FirebasePushModel mModel = new FirebasePushModel();

        Data mData = new Data();
        mData.setKey("Hello User");

        ArrayList<String> keys = new ArrayList<>();
        keys.add("cpJ6Mg85kRQ:APA91bGH5N2eGS3NvORXLAYAvvVAg2udHqifeSVXvh4bXjXmXXE4ysB3WKiIYkJe4r6RlzjHoJ8wJAFXnmAAkZdAPEyU1E91HP09IcgOrisGaDHlDR1J_xFYLRoV2O2q_BO1Mm7OuKxqPtcKlGEfwwZj4j7tV4e6ZQ");

        mModel.setData(mData);
        mModel.setRegistrationIds(keys);


        Call<ResponseBody> call = apiService.sendNotification(Constants.FCM_SERVER_KEY,"application/json",mModel);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Response<ResponseBody> mresponse = response;

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.btn_generateOTP)
    public void onButtonClick(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {


        mVoterID.setError(null);
        mMobileNumber.setError(null);

        String VoterID = mVoterID.getText().toString();
        String MobileNumber = mMobileNumber.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(VoterID)) {
            mVoterID.setError(getString(R.string.error_invalid_voter_id));
            focusView = mVoterID;
            cancel = true;
        }
        if (TextUtils.isEmpty(MobileNumber) || MobileNumber.trim().length() < 10) {
            mMobileNumber.setError(getString(R.string.error_invalid_mobile_number));
            focusView = mMobileNumber;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            startActivity(new Intent(getApplicationContext(), OTP_Activity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

}
