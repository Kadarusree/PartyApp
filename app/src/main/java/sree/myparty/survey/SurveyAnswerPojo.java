package sree.myparty.survey;

/**
 * Created by srikanthk on 7/31/2018.
 */

public class SurveyAnswerPojo  {
    public SurveyAnswerPojo(String question_id, String answer) {
        this.question_id = question_id;
        this.answer = answer;
    }

    public SurveyAnswerPojo() {
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    String question_id;
    String answer;
}
