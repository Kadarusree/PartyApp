package sree.myparty;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sree.myparty.apis.ApiClient;
import sree.myparty.apis.ApiInterface;
import sree.myparty.firebase.Data;
import sree.myparty.firebase.FirebasePushModel;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;

public class RegistartionActivity extends AppCompatActivity {


    @BindView(R.id.tv_laningText)
    TextView mLandigText;

    @BindView(R.id.edt_voterID)
    EditText mVoterID;

    @BindView(R.id.edt_mobilenumber)
    EditText mMobileNumber;
    JSONObject mObject;
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
        
        
        //Hello, this is a comment


        SessionManager mSessionManager = new SessionManager(this);
        String fb_key = mSessionManager.getFirebaseKey();

        Constants.showToast(fb_key,this);

        FirebaseInstanceId.getInstance().getToken();

       /* ApiInterface apiService =
                ApiClient.getFirebaseClient().create(ApiInterface.class);
        FirebasePushModel mModel = new FirebasePushModel();*/

        Data mData = new Data();
        mData.setKey("Hello User");

        ArrayList<String> keys = new ArrayList<>();
        keys.add("cpJ6Mg85kRQ:APA91bGH5N2eGS3NvORXLAYAvvVAg2udHqifeSVXvh4bXjXmXXE4ysB3WKiIYkJe4r6RlzjHoJ8wJAFXnmAAkZdAPEyU1E91HP09IcgOrisGaDHlDR1J_xFYLRoV2O2q_BO1Mm7OuKxqPtcKlGEfwwZj4j7tV4e6ZQ");

     /*   mModel.setData(mData);
        mModel.setRegistrationIds(keys);*/


         mObject = new JSONObject();
        try {

            mObject.put("data",new JSONObject().put("key","Hello User"));
            mObject.put("registration_ids",new JSONArray(Arrays.asList(fb_key)));

            System.out.print(mObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        DeleteBulkFromBackEnd mEnd  = new DeleteBulkFromBackEnd(new OkHttpClient());
        mEnd.execute();

        /*Call<ResponseBody> call = apiService.sendNotification(mObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Response<ResponseBody> mresponse = response;

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });*/
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


    class DeleteBulkFromBackEnd extends AsyncTask<Void, Void, String> {

        final String API_URL = Constants.FIREBASE_PUSH_API;
        final OkHttpClient mClient;

        public DeleteBulkFromBackEnd(OkHttpClient client) {
            mClient = client;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");
                RequestBody mBody = RequestBody.create(JSON,mObject.toString());
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(mBody)
                        .header("Authorization", Constants.FCM_SERVER_KEY)
                        .header("Content-Type", "application/json")
                        .build();



                okhttp3.Response response = mClient.newCall(request).execute();

                Log.d("DeleteBulkFromBackEnd", "Code: " + response.code());
                Log.d("DeleteBulkFromBackEnd", "Body: " + response.body().string());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
