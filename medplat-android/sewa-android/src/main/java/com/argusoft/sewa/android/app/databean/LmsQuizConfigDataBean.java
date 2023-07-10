package com.argusoft.sewa.android.app.databean;

public class LmsQuizConfigDataBean {

    private Boolean isCaseStudyQuestionSetType;
    private Boolean doYouWantAQuizToBeMarked;
    private Boolean allowReviewBeforeSubmission;
    private Boolean showQuizScoreQuizCompletion;
    private Integer noOfMaximumAttempts;
    private Boolean provideOptionToRestartTestFromCompletionScreen;
    private Boolean showTimeTakenToCompleteTheQuiz;
    private Boolean provideInteractiveFeedbackAfterEachQuestion;
    private LmsQuizCompletionMessage quizCompletionMessage;
    private Boolean provideOptionToLockTheQuiz;

    public Boolean getIsCaseStudyQuestionSetType() {
        return isCaseStudyQuestionSetType;
    }

    public void setIsCaseStudyQuestionSetType(Boolean isCaseStudyQuestionSetType) {
        this.isCaseStudyQuestionSetType = isCaseStudyQuestionSetType;
    }

    public Boolean getDoYouWantAQuizToBeMarked() {
        return doYouWantAQuizToBeMarked;
    }

    public void setDoYouWantAQuizToBeMarked(Boolean doYouWantAQuizToBeMarked) {
        this.doYouWantAQuizToBeMarked = doYouWantAQuizToBeMarked;
    }

    public Boolean getAllowReviewBeforeSubmission() {
        return allowReviewBeforeSubmission;
    }

    public void setAllowReviewBeforeSubmission(Boolean allowReviewBeforeSubmission) {
        this.allowReviewBeforeSubmission = allowReviewBeforeSubmission;
    }

    public Boolean getShowQuizScoreQuizCompletion() {
        return showQuizScoreQuizCompletion;
    }

    public void setShowQuizScoreQuizCompletion(Boolean showQuizScoreQuizCompletion) {
        this.showQuizScoreQuizCompletion = showQuizScoreQuizCompletion;
    }

    public Integer getNoOfMaximumAttempts() {
        return noOfMaximumAttempts;
    }

    public void setNoOfMaximumAttempts(Integer noOfMaximumAttempts) {
        this.noOfMaximumAttempts = noOfMaximumAttempts;
    }

    public Boolean getProvideOptionToRestartTestFromCompletionScreen() {
        return provideOptionToRestartTestFromCompletionScreen;
    }

    public void setProvideOptionToRestartTestFromCompletionScreen(Boolean provideOptionToRestartTestFromCompletionScreen) {
        this.provideOptionToRestartTestFromCompletionScreen = provideOptionToRestartTestFromCompletionScreen;
    }

    public Boolean getShowTimeTakenToCompleteTheQuiz() {
        return showTimeTakenToCompleteTheQuiz;
    }

    public void setShowTimeTakenToCompleteTheQuiz(Boolean showTimeTakenToCompleteTheQuiz) {
        this.showTimeTakenToCompleteTheQuiz = showTimeTakenToCompleteTheQuiz;
    }

    public Boolean getProvideInteractiveFeedbackAfterEachQuestion() {
        return provideInteractiveFeedbackAfterEachQuestion;
    }

    public void setProvideInteractiveFeedbackAfterEachQuestion(Boolean provideInteractiveFeedbackAfterEachQuestion) {
        this.provideInteractiveFeedbackAfterEachQuestion = provideInteractiveFeedbackAfterEachQuestion;
    }

    public LmsQuizCompletionMessage getQuizCompletionMessage() {
        return quizCompletionMessage;
    }

    public void setQuizCompletionMessage(LmsQuizCompletionMessage quizCompletionMessage) {
        this.quizCompletionMessage = quizCompletionMessage;
    }

    public Boolean getProvideOptionToLockTheQuiz() {
        return provideOptionToLockTheQuiz;
    }

    public void setProvideOptionToLockTheQuiz(Boolean provideOptionToLockTheQuiz) {
        this.provideOptionToLockTheQuiz = provideOptionToLockTheQuiz;
    }
}
