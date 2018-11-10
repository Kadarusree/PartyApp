package sree.myparty.SliderIndicator;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sree.myparty.DashBoard.Dashboard;

import sree.myparty.R;
import sree.myparty.RegistartionActivity;
import sree.myparty.misc.GiftFragmentDilaog;

/**
 * Created by shridhars on 12/15/2016.
 */

public class IntroActivity extends FragmentActivity {
    private boolean doubleBackToExitPressedOnce = false;

    Fragment fragment;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onelayoutframe);
        //  Crashlytics.getInstance().crash();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        mAuth = FirebaseAuth.getInstance();

        replaceFragment(0);

        final Dialog d= new Dialog(this);
        d.setContentView(R.layout.select_langauge);
        d.setCancelable(false);

        Button b =d.findViewById(R.id.lang_ok);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }


    public void replaceFragment(int position) {
        switch (position) {
            case 0:
                fragment = new CustomAnimationFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.framemain, fragment).commit();
                break;
            case 1:
                startActivity(new Intent(getApplicationContext(), RegistartionActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;


        }


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            //user signed up  with required info previosly
           /* startActivity(new Intent(getApplicationContext(), Dashboard.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();*/
            //   Toast.makeText(getApplicationContext(), "Signed user", Toast.LENGTH_SHORT).show();
        }

        // [END_EXCLUDE]
    }

    public void onBackPressed() {
        createDialog();
    }

    private void createDialog() {

        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setMessage("Are you sure you want to exit?");
        alertDlg.setCancelable(false); // We avoid that the dialong can be cancelled, forcing the user to choose one of the options
        alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        IntroActivity.super.onBackPressed();
                    }
                }
        );

        alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // We do nothing
            }

        });

        alertDlg.create().show();
    }
}
