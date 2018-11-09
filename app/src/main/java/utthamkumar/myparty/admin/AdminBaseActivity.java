package utthamkumar.myparty.admin;

import android.app.Activity;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;
import utthamkumar.myparty.DashBoard.BaseActvity;
import utthamkumar.myparty.R;
import utthamkumar.myparty.session.SessionManager;
import utthamkumar.myparty.utils.ActivityLauncher;
import utthamkumar.myparty.utils.Constants;

public class AdminBaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView nav_admin_user_name;
    CircleImageView admin_user_image;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        admin_user_image = headerView.findViewById(R.id.admin_user_image);
        nav_admin_user_name = headerView.findViewById(R.id.nav_admin_user_name);
        loadImage(AdminBaseActivity.this, admin_user_image, sessionManager.getProfilePic().toString());
        nav_admin_user_name.setText(sessionManager.getName());

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

        if (id == R.id.nav_adm_boothCommitee) {
            ActivityLauncher.launchBooths(getApplicationContext());

        } else if (id == R.id.nav_adm_voters) {
            ActivityLauncher.launchMapActivity(getApplicationContext());

        } else if (id == R.id.nav_adm_influenceperson) {
            ActivityLauncher.influencePersons(getApplicationContext());

        } else if (id == R.id.nav_adm_localIssue) {
            ActivityLauncher.localIssues(getApplicationContext());


        } else if (id == R.id.nav_adm_workdone) {
            ActivityLauncher.launchWorkDoneListActivity(getApplicationContext());

        } else if (id == R.id.nav_adm_survey) {
            ActivityLauncher.launchAdminSurveyList(getApplicationContext());

        } else if (id == R.id.nav_adm_managevol) {
            ActivityLauncher.volunteersList(getApplicationContext());

        } else if (id == R.id.nav_adm_meeting) {
            ActivityLauncher.launchMeetingsList(getApplicationContext());

        } else if (id == R.id.nav_adm_visits) {
            ActivityLauncher.launchVisitListActivity(getApplicationContext());

        } else if (id == R.id.nav_adm_boothwiseList) {
            ActivityLauncher.launchBoothwiseList(getApplicationContext());

        } else if (id == R.id.nav_adm_analysis) {
            ActivityLauncher.launchAnalysisActivity(getApplicationContext());

        }
        else if (id == R.id.nav_adm_boothwisevotersList) {
            ActivityLauncher.launcBoothWiseVotersList(getApplicationContext());

        } else if (id == R.id.nav_ad_log) {
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.isAdmin = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.isAdmin = true;
    }
}
