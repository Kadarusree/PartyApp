package sree.myparty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.philio.pinentry.PinEntryView;
import sree.myparty.DashBoard.Dashboard;
import sree.myparty.pojos.ReferalPojo;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;
import sree.myparty.utils.DailogUtill;


public class OTP_Activity extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    PinEntryView pinEntry;
    String vfId;

    ProgressDialog progressDialog;
    String voterId, mobile_number, username;
    SessionManager sessionManager;
    String refral = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        mAuth = FirebaseAuth.getInstance();
        refral = getIntent().getStringExtra(Constants.REFRAL_TEMP_SESSION);
        progressDialog = Constants.showDialog(OTP_Activity.this);
        voterId = getIntent().getStringExtra(Constants.VOTER_ID);
        mobile_number = getIntent().getStringExtra(Constants.MOBILE_NUMBER);
        username = getIntent().getStringExtra(Constants.NAME);
        vfId = getIntent().getStringExtra("VERIFICATION_ID");
        sessionManager = new SessionManager(getApplicationContext());
        ProgressBar mProgressbar = (ProgressBar) findViewById(R.id.progressBar);


        pinEntry = (PinEntryView) findViewById(R.id.inputOtp);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                pinEntry.setText(credential.getSmsCode());

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                progressDialog.dismiss();
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]

                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    DailogUtill.showDialog("Invalid phone number", getSupportFragmentManager(), getApplicationContext());

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


        };


    }

    @OnClick(R.id.btn_verify_otp)
    public void onButtonClick(View view) {


        if (pinEntry.getText().toString().length() == 6) {
            //String vfId = getIntent().getStringExtra("VERIFICATION_ID");
            verifyPhoneNumberWithCode(vfId, pinEntry.getText().toString());
            Log.d("shri", "test");

        } else {

            DailogUtill.showDialog("Enter valid OTP", getSupportFragmentManager(), getApplicationContext());
        }

    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]

        progressDialog.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

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


                                        MyApplication.getFirebaseDatabase().getReference("TempVal").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                DecimalFormat df = new DecimalFormat("0000");


                                                final String referalValue = df.format((Long.parseLong(dataSnapshot.getValue().toString()) + 1));

                                                Toast.makeText(getApplicationContext(), dataSnapshot.getValue() + "--", Toast.LENGTH_SHORT).show();
                                                final String formatedRefral = randomAlphaNumeric(3) + referalValue;

                                                MyApplication.getFirebaseDatabase().getReference("TempVal").setValue(referalValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task != null && task.isSuccessful()) {
                                                            UserDetailPojo pojo = new UserDetailPojo(voterId,
                                                                    mobile_number,
                                                                    user.getUid(),
                                                                    Constants.BOOTH_NUMBER,
                                                                    username, sessionManager.getState(), sessionManager.getPC_NAME(), sessionManager.getAC_NAME(), 0, sessionManager.getFirebaseKey(), "", Constants.QR_URL + user.getUid(), formatedRefral);
                                                            mRef.child(user.getUid()).setValue(pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        //

                                                                        if (!refral.equalsIgnoreCase("")) {

                                                                            MyApplication.getFirebaseDatabase().getReference("ReferalLinks/" + refral).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                    if (dataSnapshot.exists()) {
                                                                                        ReferalPojo referalPojo = dataSnapshot.getValue(ReferalPojo.class);
                                                                                        if (referalPojo != null) {

                                                                                            final String path = referalPojo.path;
                                                                                            final String id = referalPojo.getUser_id();
                                                                                            MyApplication.getFirebaseDatabase().getReference(path + "/Users/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                                                        UserDetailPojo userDetailPojo = dataSnapshot.getValue(UserDetailPojo.class);
                                                                                                        int points = userDetailPojo.getPoints();
                                                                                                        Map<String, Object> taskMap = new HashMap<String, Object>();
                                                                                                        taskMap.put("points", (points + 10));

                                                                                                        MyApplication.getFirebaseDatabase().getReference(path + "/Users/" + id).updateChildren(taskMap);


                                                                                                    }

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                                }
                                                                                            });


                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });


                                                                        }


                                                                        //
                                                                        ReferalPojo referalPojo = new ReferalPojo(user.getUid(), Constants.DB_PATH);
                                                                        MyApplication.getFirebaseDatabase().getReference("ReferalLinks/").child(formatedRefral).setValue(referalPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                progressDialog.dismiss();
                                                                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                                                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                });


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                        /*   */


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

    public static String randomAlphaNumeric(int count) {
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
