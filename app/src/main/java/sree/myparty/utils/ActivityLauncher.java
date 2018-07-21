package sree.myparty.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import sree.myparty.DashBoard.BaseActvity;
import sree.myparty.DashBoard.ProfileScreen;
import sree.myparty.admin.AdminLogin;

import sree.myparty.admin.AdminMap;
import sree.myparty.misc.NewsList;
import sree.myparty.misc.PostNews;
import sree.myparty.volunteer.CasteWiseVoters;
import sree.myparty.volunteer.InfluencePerson;
import sree.myparty.volunteer.Local_Issues;
import sree.myparty.volunteer.VolunteerDashboard;
import sree.myparty.volunteer.VolunteerLogin;
import sree.myparty.volunteer.VolunteerRegis;

public class ActivityLauncher {


    public static void volunteerLoginScreen(Context ctx){
        ctx.startActivity(new Intent(ctx,VolunteerLogin.class));
    }
    public static void volunteerRegistartionScreen(Context ctx){
        ctx.startActivity(new Intent(ctx,VolunteerRegis .class));
    }
    public static void adminLogin(Context ctx){
        ctx.startActivity(new Intent(ctx,AdminLogin.class));
    }

    public static void volunteerDashboard(Context ctx){
        ctx.startActivity(new Intent(ctx,VolunteerDashboard.class));
    }

    public static void influencePersons(Context ctx){
        ctx.startActivity(new Intent(ctx,InfluencePerson.class));
    }
    public static void castewiseVoters(Context ctx){
        ctx.startActivity(new Intent(ctx,CasteWiseVoters.class));
    }
    public static void localIssues(Context ctx){
        ctx.startActivity(new Intent(ctx,Local_Issues.class));
    }

    public static void postNews(Context ctx){
        ctx.startActivity(new Intent(ctx,PostNews.class));
    }

    public static void newsList(Context ctx) {
       ctx.startActivity(new Intent(ctx,NewsList.class));

    }

    public static void profileScreen(Context ctx) {
        ctx.startActivity(new Intent(ctx,ProfileScreen.class));

    }

    public static void launchMapActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx,AdminMap.class));

    }
}
