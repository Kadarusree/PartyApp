package sree.myparty;

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
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.philio.pinentry.PinEntryView;
import sree.myparty.DashBoard.Dashboard;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.utils.Constants;
import sree.myparty.utils.DailogUtill;

public class OTP_Activity extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    PinEntryView pinEntry;
    String vfId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        mAuth = FirebaseAuth.getInstance();
        progressBar = new ProgressBar(OTP_Activity.this);

        vfId = getIntent().getStringExtra("VERIFICATION_ID");

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
                progressBar.setVisibility(View.GONE);
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
            progressBar.setVisibility(View.VISIBLE);
            String vfId = getIntent().getStringExtra("VERIFICATION_ID");
            Log.d("shri", getIntent().getStringExtra("VERIFICATION_ID"));
            verifyPhoneNumberWithCode(vfId, pinEntry.getText().toString());


        } else {

            DailogUtill.showDialog("Enter valid OTP", getSupportFragmentManager(), getApplicationContext());
        }

    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]

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
                                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                    } else {
                                        UserDetailPojo pojo = new UserDetailPojo("shri", "metraskar", "123456");
                                        mRef.child(user.getUid()).setValue(pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                                                }
                                            }
                                        });

                                        Toast.makeText(getApplicationContext(), "not exists", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

                                }
                            });


                            // [START_EXCLUDE]
                            //updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            progressBar.setVisibility(View.GONE);

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
                            DailogUtill.showDialog("Signed failed.", getSupportFragmentManager(), getApplicationContext());

                            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                            // [END_EXCLUDE]
                        }
                    }
                });
    }

}
