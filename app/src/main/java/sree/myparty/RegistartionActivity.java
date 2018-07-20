package sree.myparty;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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
import java.util.concurrent.TimeUnit;

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
import sree.myparty.DashBoard.Dashboard;
import sree.myparty.apis.ApiClient;
import sree.myparty.apis.ApiInterface;
import sree.myparty.firebase.Data;
import sree.myparty.firebase.FirebasePushModel;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;
import sree.myparty.utils.DailogUtill;
import sree.myparty.utils.SelectPC;

public class RegistartionActivity extends AppCompatActivity {


    @BindView(R.id.tv_laningText)
    TextView mLandigText;

    @BindView(R.id.edt_voterID)
    EditText mVoterID;

    @BindView(R.id.edt_userName)
    EditText edt_userName;

    @BindView(R.id.edt_mobilenumber)
    EditText mMobileNumber;
    JSONObject mObject;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean mVerificationInProgress = false;
    private static final String TAG = "PhoneAuthActivity";
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    private final String VERIFICATION_ID = "VERIFICATION_ID";

    private String mVerificationId;

    ProgressDialog progressDialog;
    SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registartion);
        ButterKnife.bind(this);
        mLandigText.setText("Voter 360\u00b0\n Telangana State");
        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        mLandigText.setTypeface(tf);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = Constants.showDialog(RegistartionActivity.this);
        mSessionManager = new SessionManager(this);
        String fb_key = mSessionManager.getFirebaseKey();
        Constants.showToast(fb_key, this);
        FirebaseInstanceId.getInstance().getToken();



        Data mData = new Data();
        mData.setKey("Hello User");

        ArrayList<String> keys = new ArrayList<>();
        keys.add(fb_key);

     /*   mModel.setData(mData);
        mModel.setRegistrationIds(keys);*/


        mObject = new JSONObject();
        try {

            mObject.put("data", new JSONObject().put("key", "Sinch"));
            mObject.put("registration_ids", new JSONArray(Arrays.asList(fb_key)));

            System.out.print(mObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {


                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]

                // Update the UI and attempt sign in with the phone credential
                //  updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Automatic detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                progressDialog.dismiss();
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mMobileNumber.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                //updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]


            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                progressDialog.dismiss();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                Intent intent = new Intent(getApplicationContext(), OTP_Activity.class);
                intent.putExtra(VERIFICATION_ID, verificationId);
                intent.putExtra(Constants.VOTER_ID, mVoterID.getText().toString().trim());
                intent.putExtra(Constants.NAME, edt_userName.getText().toString().trim());
                intent.putExtra(Constants.MOBILE_NUMBER, mMobileNumber.getText().toString().trim());
                startActivity(intent);
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                //updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }


    @OnClick(R.id.btn_generateOTP)
    public void onButtonClick(View v) {
        attemptRegistration();

        //   startActivity(new Intent(getApplicationContext(),Dashboard.class));

    }

    private void attemptRegistration() {


        mVoterID.setError(null);
        mMobileNumber.setError(null);
        edt_userName.setError(null);
        String VoterID = mVoterID.getText().toString();
        String MobileNumber = mMobileNumber.getText().toString();
        String userName = edt_userName.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(userName)) {
            edt_userName.setError(getString(R.string.error_invalid_user_name));
            focusView = edt_userName;
            cancel = true;
        }
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
            progressDialog.show();

            startPhoneNumberVerification(MobileNumber);
            // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            //user signed up  with required info previosly
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            Toast.makeText(getApplicationContext(), "Signed user", Toast.LENGTH_SHORT).show();
        }

        // [END_EXCLUDE]
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }


/*    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }*/

   /* private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            //updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                         //   updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }*/

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");


                            final FirebaseUser user = task.getResult().getUser();
                            final DatabaseReference mRef = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Users");
                            mRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                    } else {

                                        UserDetailPojo pojo = new UserDetailPojo(mVoterID.getText().toString().trim(),
                                                mMobileNumber.getText().toString().trim(),
                                                user.getUid(),
                                                "",
                                                edt_userName.getText().toString().trim(), mSessionManager.getState(), mSessionManager.getPC_NAME(), mSessionManager.getAC_NAME(), 0, mSessionManager.getFirebaseKey(), "", Constants.QR_URL + user.getUid());
                                        mRef.child(user.getUid()).setValue(pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                                                }
                                            }
                                        });


                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

                                }
                            });


                            // [START_EXCLUDE]
                            //updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            progressDialog.dismiss();

                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //mVerificationField.setError("Invalid code.");
                                DailogUtill.showDialog("Invalid code.", getSupportFragmentManager(), getApplicationContext());


                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //   updateUI(STATE_SIGNIN_FAILED);
                            //  DailogUtill.showDialog("Signed failed.", getSupportFragmentManager(), getApplicationContext());

                            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            SelectPC mPc = new SelectPC();
            mPc.show(getFragmentManager(), "SelectPC");
        }
    }
}
