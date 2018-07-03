package sree.myparty.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import sree.myparty.admin.AdminLogin;
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
}
