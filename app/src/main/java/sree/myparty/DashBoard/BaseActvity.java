package sree.myparty.DashBoard;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;

public abstract class BaseActvity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView mProfilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_actvity);
        mProfilePic = findViewById(R.id.nav_imageView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragement(new HomeFragment());

        getAllFirebaseIDs();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_actvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.id_card) {
            ActivityLauncher.profileScreen(this);
        } else if (id == R.id.nav_gallery) {
            ActivityLauncher.newsList(this);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            FirebaseAuth  auth= FirebaseAuth.getInstance();
            auth.signOut();


            SessionManager mSessionManager = new SessionManager(this);
            mSessionManager.logout();

            this.finish();

        } else if (id == R.id.nav_share) {
          shareApp(new SessionManager(this).getName()+": Refered you to join My Party app..");

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


   public  void shareApp( String link)
    {

      /*  Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,link);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);*/


        String shareBody = link+"\n"+"https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" -Download App from playstore");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody+"\n"+"Promo code-"+new SessionManager(this).getREFERAL_CODE());

        Intent i=new Intent(Intent.createChooser(sharingIntent, "Share via"));
        startActivity(i);


    }
    public void replaceFragement(Fragment fg) {
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.content_frame, fg);
        mFragmentTransaction.commit();
    }

    public void getAllFirebaseIDs() {
        final ArrayList<String> fcmIds = new ArrayList<>();
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fcmIds.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserDetailPojo MUser = postSnapshot.getValue(UserDetailPojo.class);
                    fcmIds.add(MUser.getFcm_id());
                }

                Toast.makeText(getApplicationContext(), fcmIds.size() + "", Toast.LENGTH_LONG).show();
            Constants.fcm_ids = fcmIds;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
