package com.nbird.multiplayerquiztrivia.Model;

public class questionHolder {
    private String questionTextView,option1,option2,option3,option4,correctAnswer,questionPicture,songURL,imageURL;
    private int setNo;

    public questionHolder() {
    }

    public questionHolder(String questionTextView, String option1, String option2, String option3, String option4, String correctAnswer, int setNo) {
        this.questionTextView = questionTextView;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
        this.setNo = setNo;
    }

    public questionHolder(String questionTextView, String option1, String option2, String option3, String option4, String correctAnswer, int setNo, String questionPicture) {
        this.questionTextView = questionTextView;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
        this.setNo = setNo;
        this.questionPicture=questionPicture;
    }

    public questionHolder(String questionTextView, String option1, String option2, String option3, String option4, String correctAnswer, int setNo, String imageURL,String songURL) {
        this.questionTextView = questionTextView;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
        this.setNo = setNo;
        this.imageURL=imageURL;
        this.songURL=songURL;
    }





    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSongURL() {
        return songURL;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

    public String getQuestionPicture() {
        return questionPicture;
    }

    public void setQuestionPicture(String questionPicture) {
        this.questionPicture = questionPicture;
    }

    public String getQuestionTextView() {
        return questionTextView;
    }

    public void setQuestionTextView(String questionTextView) {
        this.questionTextView = questionTextView;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getSetNo() {
        return setNo;
    }

    public void setSetNo(int setNo) {
        this.setNo = setNo;
    }
}
