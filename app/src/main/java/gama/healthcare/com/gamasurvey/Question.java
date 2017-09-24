package gama.healthcare.com.gamasurvey;

/**
 * Created by Humz on 10/09/2017.
 */

class Question
{
    private String questionText;

    private boolean isMultipleChoice;

    private String answerString;

    private Integer answerNumber;

    Question(String questionText, boolean isMultipleChoice, String answerString, Integer answerNumber)
    {
        this.questionText = questionText;
        this.isMultipleChoice = isMultipleChoice;
        this.answerString = answerString;
        this.answerNumber = answerNumber;
    }

    Question()
    {}


    public String getQuestionText()
    {
        return questionText;
    }

    public void setQuestionText(String questionText)
    {
        this.questionText = questionText;
    }

    public boolean isMultipleChoice()
    {
        return isMultipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice)
    {
        isMultipleChoice = multipleChoice;
    }

    public String getAnswerString()
    {
        return answerString;
    }

    public void setAnswerString(String answerString)
    {
        this.answerString = answerString;
    }

    public Integer getAnswerNumber()
    {
        return answerNumber;
    }

    public void setAnswerNumber(Integer answerNumber)
    {
        this.answerNumber = answerNumber;
    }

}