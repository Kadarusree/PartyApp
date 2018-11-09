package utthamkumar.myparty.survey;

/**
 * Created by srikanthk on 7/30/2018.
 */

public class SurveyPojo {

    public String getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(String surveyID) {
        this.surveyID = surveyID;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(String surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    public String getSurveyOption1() {
        return surveyOption1;
    }

    public void setSurveyOption1(String surveyOption1) {
        this.surveyOption1 = surveyOption1;
    }

    public String getSurveyOption2() {
        return surveyOption2;
    }

    public void setSurveyOption2(String surveyOption2) {
        this.surveyOption2 = surveyOption2;
    }

    public String getSurveyOption3() {
        return surveyOption3;
    }

    public void setSurveyOption3(String surveyOption3) {
        this.surveyOption3 = surveyOption3;
    }

    public String getSurveryPostedBy() {
        return surveryPostedBy;
    }

    public void setSurveryPostedBy(String surveryPostedBy) {
        this.surveryPostedBy = surveryPostedBy;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    String surveyID, surveyName, surveyQuestion, surveyOption1, surveyOption2, surveyOption3, surveryPostedBy;
    boolean isActive;

    public boolean isOption1Selected() {
        return option1Selected;
    }

    public void setOption1Selected(boolean option1Selected) {
        this.option1Selected = option1Selected;
    }

    public boolean isOption2Selected() {
        return option2Selected;
    }

    public void setOption2Selected(boolean option2Selected) {
        this.option2Selected = option2Selected;
    }

    public boolean isOption3Selected() {
        return option3Selected;
    }

    public void setOption3Selected(boolean option3Selected) {
        this.option3Selected = option3Selected;
    }

    boolean option1Selected = false;
    boolean option2Selected = false;
    boolean option3Selected = false;

    public boolean isCons() {
        return isCons;
    }

    public void setCons(boolean cons) {
        isCons = cons;
    }

    public String getBoothNumber() {
        return boothNumber;
    }

    public void setBoothNumber(String boothNumber) {
        this.boothNumber = boothNumber;
    }

    boolean isCons;
    String boothNumber;

    public SurveyPojo() {
    }

    public SurveyPojo(String surveyID, String surveyName, String surveyQuestion, String surveyOption1, String surveyOption2, String surveyOption3, String surveryPostedBy, boolean isActive, boolean isCons, String boothNumber) {
        this.surveyID = surveyID;
        this.surveyName = surveyName;
        this.surveyQuestion = surveyQuestion;
        this.surveyOption1 = surveyOption1;
        this.surveyOption2 = surveyOption2;
        this.surveyOption3 = surveyOption3;
        this.surveryPostedBy = surveryPostedBy;
        this.isActive = isActive;
        this.isCons = isCons;
        this.boothNumber = boothNumber;
    }
}
