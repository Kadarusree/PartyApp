package utthamkumar.myparty.utils;

import android.content.Context;
import android.content.Intent;

import utthamkumar.myparty.DashBoard.ProfileScreen;
import utthamkumar.myparty.admin.AdminDashboard;
import utthamkumar.myparty.admin.AdminLogin;

import utthamkumar.myparty.admin.AdminMap;
import utthamkumar.myparty.admin.AnalysisActivity;
import utthamkumar.myparty.admin.AnalysisMainActivity;
import utthamkumar.myparty.admin.BoothCommitteList;
import utthamkumar.myparty.admin.BoothCommitteeMap;
import utthamkumar.myparty.admin.BoothList;
import utthamkumar.myparty.admin.BoothWiseVoterAnalysis;
import utthamkumar.myparty.admin.BoothWiseVotesAnalysis;
import utthamkumar.myparty.admin.BoothwiseList;
import utthamkumar.myparty.admin.BoothwiseVoterList;
import utthamkumar.myparty.admin.InfluencePersonAdminView;
import utthamkumar.myparty.admin.LocalIssuesAdminView;
import utthamkumar.myparty.admin.MeetingAttendence;
import utthamkumar.myparty.admin.MeetingsActivity;
import utthamkumar.myparty.admin.MeetingsListActivity;
import utthamkumar.myparty.admin.ParticularBooth;
import utthamkumar.myparty.admin.PresentTrend;
import utthamkumar.myparty.admin.VisitActivity;
import utthamkumar.myparty.admin.VisitsListActivity;
import utthamkumar.myparty.admin.VolunteerList;
import utthamkumar.myparty.admin.VoterEditForm;
import utthamkumar.myparty.admin.VotesAnalysis;
import utthamkumar.myparty.admin.WorkDoneActivity;
import utthamkumar.myparty.admin.WorksListActivity;
import utthamkumar.myparty.misc.NewsList;
import utthamkumar.myparty.misc.PostNews;
import utthamkumar.myparty.survey.CreateSurvey;
import utthamkumar.myparty.survey.SurveyAdminList;
import utthamkumar.myparty.survey.SurveyList;
import utthamkumar.myparty.volunteer.CasteWiseVoters;
import utthamkumar.myparty.volunteer.InfluencePerson;
import utthamkumar.myparty.volunteer.Local_Issues;
import utthamkumar.myparty.volunteer.NewsAcceptList;
import utthamkumar.myparty.volunteer.VolunteerDashboard;
import utthamkumar.myparty.volunteer.VolunteerLogin;
import utthamkumar.myparty.volunteer.VolunteerRegis;

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
