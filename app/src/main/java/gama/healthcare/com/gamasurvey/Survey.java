package gama.healthcare.com.gamasurvey;

import java.util.List;

/**
 * Created by Humz on 10/09/2017.
 */

public class Survey
{
    private String title;

    private List<Question> questionList;

    Survey()
    {

    }

    public Survey(String title, List<Question> questionList)
    {
        this.title = title;
        this.questionList = questionList;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public List<Question> getQuestionList()
    {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList)
    {
        this.questionList = questionList;
    }
}
