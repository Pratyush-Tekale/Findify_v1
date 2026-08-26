package com.findify.model;

public class VerificationQuestion {

    private int questionId;
    private int foundId;
    private String questionText;

    // Only ever populated when the finder/admin needs it server-side
    // (scoring). Never serialize this to the public claim form.
    private String correctAnswer;

    public VerificationQuestion() {
    }

    public VerificationQuestion(int foundId, String questionText, String correctAnswer) {
        this.foundId = foundId;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getFoundId() {
        return foundId;
    }

    public void setFoundId(int foundId) {
        this.foundId = foundId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
