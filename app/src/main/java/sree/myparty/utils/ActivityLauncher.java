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
import sree.myparty.admin.BoothCommitteeMap;
import sree.myparty.admin.BoothList;
import sree.myparty.admin.BoothWiseVoterAnalysis;
import sree.myparty.admin.BoothWiseVotesAnalysis;
import sree.myparty.admin.BoothwiseList;
import sree.myparty.admin.BoothwiseVoterList;
import sree.myparty.admin.InfluencePersonAdminView;
import sree.myparty.admin.LocalIssuesAdminView;
import sree.myparty.admin.MeetingAttendence;
import sree.myparty.admin.MeetingsActivity;
import sree.myparty.admin.MeetingsListActivity;
import sree.myparty.admin.ParticularBooth;
import sree.myparty.admin.PresentTrend;
import sree.myparty.admin.VisitActivity;
import sree.myparty.admin.VisitsListActivity;
import sree.myparty.admin.VolunteerList;
import sree.myparty.admin.VoterEditForm;
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
import sree.myparty.volunteer.NewsAcceptList;
import sree.myparty.volunteer.VolunteerDashboard;
import sree.myparty.volunteer.VolunteerLogin;
import sree.myparty.volunteer.VolunteerRegis;

public class ActivityLauncher {


    public static void volunteerLoginScreen(Context ctx) {
        ctx.startActivity(new Intent(ctx, VolunteerLogin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void volunteerRegistartionScreen(Context ctx) {
        ctx.startActivity(new Intent(ctx, VolunteerRegis.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void adminLogin(Context ctx) {
        ctx.startActivity(new Intent(ctx, AdminLogin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void volunteerDashboard(Context ctx) {
        ctx.startActivity(new Intent(ctx, VolunteerDashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void influencePersons(Context ctx) {
        ctx.startActivity(new Intent(ctx, InfluencePerson.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void influencePersonsAdminView(Context ctx) {
        ctx.startActivity(new Intent(ctx, InfluencePersonAdminView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    public static void castewiseVoters(Context ctx) {
        ctx.startActivity(new Intent(ctx, CasteWiseVoters.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void localIssues(Context ctx) {
        ctx.startActivity(new Intent(ctx, Local_Issues.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void localIssuesAdminView(Context ctx) {
        ctx.startActivity(new Intent(ctx, LocalIssuesAdminView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void postNews(Context ctx) {
        ctx.startActivity(new Intent(ctx, PostNews.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void newsAcceptList(Context ctx) {
        ctx.startActivity(new Intent(ctx, NewsAcceptList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void newsList(Context ctx) {
        ctx.startActivity(new Intent(ctx, NewsList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public static void profileScreen(Context ctx) {
        ctx.startActivity(new Intent(ctx, ProfileScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public static void launchMapActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, AdminMap.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public static void launchAdminDB(Context ctx) {
        ctx.startActivity(new Intent(ctx, AdminDashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }


    public static void launchAttendenceActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, MeetingAttendence.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public static void volunteersList(Context ctx) {
        ctx.startActivity(new Intent(ctx, VolunteerList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchMeetingsActivity(Context activity) {
        activity.startActivity(new Intent(activity, MeetingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public static void launchAnalysisActivity(Context activity) {
        activity.startActivity(new Intent(activity, AnalysisMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchMeetingsList(Context activity) {
        activity.startActivity(new Intent(activity, MeetingsListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchVisitListActivity(Context activity) {
        activity.startActivity(new Intent(activity, VisitsListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchVisitActivity(Context activity) {
        activity.startActivity(new Intent(activity, VisitActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchWorkDoneActivity(Context activity) {
        activity.startActivity(new Intent(activity, WorkDoneActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchWorkDoneListActivity(Context activity) {
        activity.startActivity(new Intent(activity, WorksListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchSurveyList(Context activity) {
        activity.startActivity(new Intent(activity, SurveyList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchCreateSurvey(Context activity) {
        activity.startActivity(new Intent(activity, CreateSurvey.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchAdminSurveyList(Context ctx) {
        ctx.startActivity(new Intent(ctx, SurveyAdminList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchVotersAnalysis(Context activity) {
        activity.startActivity(new Intent(activity, AnalysisActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchParticularBooth(Context activity) {
        activity.startActivity(new Intent(activity, ParticularBooth.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchVotesAnalysis(Context activity) {
        activity.startActivity(new Intent(activity, VotesAnalysis.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchBooths(Context activity) {
        activity.startActivity(new Intent(activity, BoothCommitteList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchPresentTrend(Context activity) {
        activity.startActivity(new Intent(activity, PresentTrend.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchBoothWiseVotesAnalysyis(Context activity) {
        activity.startActivity(new Intent(activity, BoothWiseVotesAnalysis.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchBoothWiseVotersAnalysyis(Context activity) {
        activity.startActivity(new Intent(activity, BoothWiseVoterAnalysis.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchBoothList(Context activity) {
        activity.startActivity(new Intent(activity, BoothList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchBoothsOnMap(Context activity) {
        activity.startActivity(new Intent(activity, BoothCommitteeMap.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void launchBoothwiseList(Context applicationContext) {
        applicationContext.startActivity(new Intent(applicationContext, BoothwiseList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public static void launcBoothWiseVotersList(Context applicationContext) {
        applicationContext.startActivity(new Intent(applicationContext, BoothwiseVoterList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public static void launchVoterEdit(Context mContext) {
        mContext.startActivity(new Intent(mContext, VoterEditForm.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }
}
