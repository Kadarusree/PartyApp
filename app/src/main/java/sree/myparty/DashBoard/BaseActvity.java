package sree.myparty.DashBoard;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.beans.WishMePojo;
import sree.myparty.chat.UserListActicity;
import sree.myparty.constuecies.Booths;
import sree.myparty.constuecies.Parser;
import sree.myparty.misc.GiftFragmentDilaog;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.ActivityLauncher;
import sree.myparty.utils.Constants;
import sree.myparty.utils.VolunteerSessionManager;
import sree.myparty.volunteer.VolunteerBaseActivity;

public abstract class BaseActvity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    TextView nav_admin_name;
    CircleImageView admin_imageView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_actvity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(getApplicationContext());
        Parser mParser = new Parser(this);
        /*Booths Booths = mParser.getBooths("Khairatabad");
        DatabaseReference reference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH).child("Booths");

        reference.setValue(Booths);*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(BaseActvity.this, VideoActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        admin_imageView = headerView.findViewById(R.id.admin_imageView);
        nav_admin_name = headerView.findViewById(R.id.nav_admin_name);

        loadImage(BaseActvity.this, admin_imageView, sessionManager.getProfilePic().toString());
        nav_admin_name.setText(sessionManager.getName());

        replaceFragement(new HomeFragment());

        getAllFirebaseIDs();

        hideAdminOptions();
        hideVolOptions();
    }

    public static void loadImage(final Activity context, ImageView imageView, String url) {
        if (context == null || context.isDestroyed()) return;

        //placeHolderUrl=R.drawable.ic_user;
        //errorImageUrl=R.drawable.ic_error;
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .placeholder(R.drawable.avatar) //this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.
                .error(R.drawable.ic_warning)//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.ALL) //using to load into cache then second time it will load fast.
                .animate(R.anim.fade_in) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                .fitCenter()//this method help to fit image into center of your ImageView
                .into(imageView); //pass imageView reference to appear the image.
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.id_card) {
            ActivityLauncher.profileScreen(this);
        } else if (id == R.id.nav_gallery) {
            ActivityLauncher.newsList(this);
        } /*else if (id == R.id.nav_workdone) {
            ActivityLauncher.volunteerRegistartionScreen(getApplicationContext());

        }*/ else if (id == R.id.nav_chat) {
            startActivity(new Intent(getApplicationContext(), UserListActicity.class));

        } else if (id == R.id.id_meet) {
            ActivityLauncher.launchMeetingsList(getApplicationContext());

        } else if (id == R.id.nav_survey) {
            ActivityLauncher.launchSurveyList(getApplicationContext());

        } else if (id == R.id.nav_vol_lregis) {
            ActivityLauncher.volunteerRegistartionScreen(getApplicationContext());

        } else if (id == R.id.nav_postNews) {
            ActivityLauncher.postNews(getApplicationContext());

        } else if (id == R.id.nav_vol_login) {
            if (new VolunteerSessionManager(getApplicationContext()).hasActiveSession()) {
                ActivityLauncher.volunteerDashboard(getApplicationContext());
            } else {
                ActivityLauncher.volunteerLoginScreen(getApplicationContext());
            }

        } else if (id == R.id.nav_admi_login) {
            ActivityLauncher.adminLogin(getApplicationContext());

        } else if (id == R.id.nav_manage) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
          DatabaseReference referenced =  MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Users/" + auth.getUid());
            if(FirebaseInstanceId.getInstance().getToken()!=null) {
                sessionManager.storeFirebaseKey(FirebaseInstanceId.getInstance().getToken());
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("fcm_id", "");
                referenced.updateChildren(taskMap);
            }

            SessionManager mSessionManager = new SessionManager(this);
            mSessionManager.logout();

            this.finish();

        } else if (id == R.id.nav_share) {
            shareApp(new SessionManager(this).getName() + ": Refered you to join My Party app..");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideVolOptions() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().removeGroup(1);
    }

    public void checkWish() {
        FirebaseDatabase.getInstance().getReference(Constants.WISH_ME_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                WishMePojo pojo = dataSnapshot.getValue(WishMePojo.class);
                if (pojo != null && pojo.isShow() == true) {

                    try {
                        if (pojo.getCode()!=0) {
                            GiftFragmentDilaog dFragment = new GiftFragmentDilaog(pojo.getPath(), pojo.getCode());
                            dFragment.setCancelable(false); //don't close when touch outside
                            dFragment.show(getSupportFragmentManager(), "Dialog Fragment");
                        }
                    } catch (Exception e) {

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void hideAdminOptions() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().removeGroup(2);

        //   navigationView.getMenu().add(1)
        //nav_Menu.findItem(R.id.adminOptions).setVisible(false);
    }

    public void shareApp(String link) {

      /*  Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,link);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);*/


        String shareBody = link + "\n" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " -Download App from playstore");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + "\n" + "Promo code-" + new SessionManager(this).getREFERAL_CODE());

        Intent i = new Intent(Intent.createChooser(sharingIntent, "Share via"));
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

                //      Toast.makeText(getApplicationContext(), fcmIds.size() + "", Toast.LENGTH_LONG).show();
                Constants.fcm_ids = fcmIds;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
