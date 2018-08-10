package sree.myparty.volunteer;

import android.app.Activity;
import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import sree.myparty.DashBoard.ProfileScreen;
import sree.myparty.R;
import sree.myparty.misc.NewsList;
import sree.myparty.misc.PostNews;
import sree.myparty.utils.VolunteerSessionManager;

public class VolunteerBaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


   TextView nav_vol_name;

    VolunteerSessionManager volunteerSessionManager;



    CircleImageView vol_imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_base);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        volunteerSessionManager=new VolunteerSessionManager(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_vol_name=headerView.findViewById(R.id.nav_vol_name);
        vol_imageView=headerView.findViewById(R.id.vol_imageView);
        loadImage(VolunteerBaseActivity.this,vol_imageView,volunteerSessionManager.getProfilePic().toString());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.volunteer_base, menu);
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

        if (id == R.id.nav_vol_castwisevoters) {

            Intent intent=new Intent(VolunteerBaseActivity.this,CasteWiseVoters.class);
            startActivity(intent);

        } else if (id == R.id.nav_vol_influencepersson) {
            Intent intent=new Intent(VolunteerBaseActivity.this,InfluencePerson.class);
            startActivity(intent);

        } else if (id == R.id.nav_vol_localIssue) {
            Intent intent=new Intent(VolunteerBaseActivity.this,Local_Issues.class);
            startActivity(intent);
        }  else if (id == R.id.nav_vol_logout) {
            volunteerSessionManager.logout();
            finish();

        } else if (id == R.id.nav_vol_postnews) {
            Intent intent=new Intent(VolunteerBaseActivity.this,PostNews.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        nav_vol_name.setText(volunteerSessionManager.getVolName());
    }
}
