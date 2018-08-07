package sree.myparty.utils;

import android.content.Context;
import android.content.Intent;

import sree.myparty.DashBoard.ProfileScreen;
import sree.myparty.admin.AdminDashboard;
import sree.myparty.admin.AdminLogin;

import sree.myparty.admin.AdminMap;
import sree.myparty.admin.AnalysisActivity;
import sree.myparty.admin.AnalysisMainActivity;
import sree.myparty.admin.BoothCommitteList;
import sree.myparty.admin.MeetingAttendence;
import sree.myparty.admin.MeetingsActivity;
import sree.myparty.admin.MeetingsListActivity;
import sree.myparty.admin.ParticularBooth;
import sree.myparty.admin.VisitActivity;
import sree.myparty.admin.VisitsListActivity;
import sree.myparty.admin.VolunteerList;
import sree.myparty.admin.VotesAnalysis;
import sree.myparty.admin.WorkDoneActivity;
import sree.myparty.admin.WorksListActivity;
import sree.myparty.misc.NewsList;
import sree.myparty.misc.PostNews;
import sree.myparty.survey.CreateSurvey;
import sree.myparty.survey.SurveyAdminList;
import sree.myparty.survey.SurveyList;
import sree.myparty.volunteer.CasteWiseVoters;
import sree.myparty.volunteer.InfluencePerson;
import sree.myparty.volunteer.Local_Issues;
import sree.myparty.volunteer.VolunteerDashboard;
import sree.myparty.volunteer.VolunteerLogin;
import sree.myparty.volunteer.VolunteerRegis;

public class ActivityLauncher {


    public static void volunteerLoginScreen(Context ctx) {
        ctx.startActivity(new Intent(ctx, VolunteerLogin.class));
    }

    public static void volunteerRegistartionScreen(Context ctx) {
        ctx.startActivity(new Intent(ctx, VolunteerRegis.class));
    }

    public static void adminLogin(Context ctx) {
        ctx.startActivity(new Intent(ctx, AdminLogin.class));
    }

    public static void volunteerDashboard(Context ctx) {
        ctx.startActivity(new Intent(ctx, VolunteerDashboard.class));
    }

    public static void influencePersons(Context ctx) {
        ctx.startActivity(new Intent(ctx, InfluencePerson.class));
    }

    public static void castewiseVoters(Context ctx) {
        ctx.startActivity(new Intent(ctx, CasteWiseVoters.class));
    }

    public static void localIssues(Context ctx) {
        ctx.startActivity(new Intent(ctx, Local_Issues.class));
    }

    public static void postNews(Context ctx) {
        ctx.startActivity(new Intent(ctx, PostNews.class));
    }

    public static void newsList(Context ctx) {
        ctx.startActivity(new Intent(ctx, NewsList.class));

    }

    public static void profileScreen(Context ctx) {
        ctx.startActivity(new Intent(ctx, ProfileScreen.class));

    }

    public static void launchMapActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, AdminMap.class));

    }

    public static void launchAdminDB(Context ctx) {
        ctx.startActivity(new Intent(ctx, AdminDashboard.class));

    }


    public static void launchAttendenceActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, MeetingAttendence.class));

    }

    public static void volunteersList(Context ctx) {
        ctx.startActivity(new Intent(ctx, VolunteerList.class));
    }

    public static void launchMeetingsActivity(Context activity) {
        activity.startActivity(new Intent(activity, MeetingsActivity.class));

    }

    public static void launchAnalysisActivity(Context activity) {
        activity.startActivity(new Intent(activity, AnalysisMainActivity.class));
    }

    public static void launchMeetingsList(Context activity) {
        activity.startActivity(new Intent(activity, MeetingsListActivity.class));
    }

    public static void launchVisitListActivity(Context activity) {
        activity.startActivity(new Intent(activity, VisitsListActivity.class));
    }

    public static void launchVisitActivity(Context activity) {
        activity.startActivity(new Intent(activity, VisitActivity.class));
    }

    public static void launchWorkDoneActivity(Context activity) {
        activity.startActivity(new Intent(activity, WorkDoneActivity.class));
    }

    public static void launchWorkDoneListActivity(Context activity) {
        activity.startActivity(new Intent(activity, WorksListActivity.class));
    }

    public static void launchSurveyList(Context activity) {
        activity.startActivity(new Intent(activity, SurveyList.class));
    }

    public static void launchCreateSurvey(Context activity) {
        activity.startActivity(new Intent(activity, CreateSurvey.class));
    }

    public static void launchAdminSurveyList(Context ctx) {
        ctx.startActivity(new Intent(ctx, SurveyAdminList.class));
    }

    public static void launchVotersAnalysis(Context activity) {
        activity.startActivity(new Intent(activity, AnalysisActivity.class));
    }

    public static void launchParticularBooth(Context activity) {
        activity.startActivity(new Intent(activity, ParticularBooth.class));
    }

    public static void launchVotesAnalysis(Context activity) {
        activity.startActivity(new Intent(activity, VotesAnalysis.class));
    }

    public static void launchBooths(Context activity) {
        activity.startActivity(new Intent(activity, BoothCommitteList.class));
    }
}
