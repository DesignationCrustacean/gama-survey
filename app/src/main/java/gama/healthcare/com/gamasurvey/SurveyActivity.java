package gama.healthcare.com.gamasurvey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity
{

    private Survey survey;
    private TextView questionCounter, questionTitle;
    private Button back, next, exit, nextUser;
    private EditText answer;
    private RadioGroup radioGroup;
    private RatingBar ratingBar;
    private boolean surveyInProgress;

    private int currentQuestionIndex = 0;

    private List<Question> answeredQuestions = new ArrayList<>();
    private ConstraintLayout congratsView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        questionCounter = (TextView) findViewById(R.id.tv_questionCounter);
        questionTitle = (TextView) findViewById(R.id.tv_questionTitle);
        back = (Button) findViewById(R.id.iv_prev);
        next = (Button) findViewById(R.id.iv_next);
        answer = (EditText) findViewById(R.id.et_answer);
        ratingBar = (RatingBar) findViewById(R.id.rb_ratingBar);
        radioGroup = (RadioGroup) findViewById(R.id.rg_radioGroup);
        congratsView = (ConstraintLayout) findViewById(R.id.cl_congrats);
        exit = (Button) findViewById(R.id.bt_exit);
        nextUser = (Button) findViewById(R.id.bt_nextuser);


        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                surveyInProgress = true;
                Question question = survey.getQuestionList().get(currentQuestionIndex);
                if(question.isMultipleChoice())
                {
                    int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));

                    if(index < 0)
                    {
                        Toast.makeText(getApplicationContext(), "Please select an answer!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        question.setAnswerNumber(index);
                        submitAndContinue(question);
                    }
                }
                else
                {
                    if(answer.getText().toString().trim().matches(""))
                    {
                        Toast.makeText(getApplicationContext(), "Please write an answer!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        question.setAnswerString(answer.getText().toString().trim());
                        submitAndContinue(question);
                    }
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentQuestionIndex--;
                updateProgressBar();
                showQuestion(currentQuestionIndex);

            }
        });


        exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        nextUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (getIntent() != null && getIntent().hasExtra("survey"))
        {
            String surveyTitle = getIntent().getStringExtra("survey");

            survey = new Survey();
            try
            {
                survey = getSurvey(surveyTitle);
            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }

            showQuestion(currentQuestionIndex);
            //do survey


        }
        else
        {
            //error message
        }


    }

    private void submitAndContinue(Question question)
    {

        for(Question q: answeredQuestions)
        {
            if(q.getQuestionText().equals(question.getQuestionText()))
            {
                answeredQuestions.remove(q);
            }
        }
        answeredQuestions.add(question);
        //display progress bar
        updateProgressBar();

        if(survey.getQuestionList().size()-1 != currentQuestionIndex)
        {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        }
        else
        {
            surveyInProgress = false;
            questionTitle.setVisibility(View.INVISIBLE);
            questionCounter.setVisibility(View.INVISIBLE);
            congratsView.setVisibility(View.VISIBLE);
            next.setVisibility(View.INVISIBLE);
            back.setVisibility(View.INVISIBLE);
            radioGroup.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.GONE);
        }
    }

    private void updateProgressBar()
    {
        ratingBar.setMax(survey.getQuestionList().size());
        ratingBar.setNumStars(survey.getQuestionList().size());
        ratingBar.setRating(currentQuestionIndex+1);
    }

    private void showQuestion(int questionIndex)
    {
        radioGroup.clearCheck();
        answer.setText("");

        if(currentQuestionIndex == 0)
        {
            back.setVisibility(View.INVISIBLE);
            surveyInProgress = false;
        }
        else
        {
            back.setVisibility(View.VISIBLE);
        }
        if(currentQuestionIndex+1 == survey.getQuestionList().size())
        {
            next.setText("FINISH");
        }
        Question question = survey.getQuestionList().get(questionIndex);

        boolean isMultipleChoice = question.isMultipleChoice();

        //display question text
        questionTitle.setText(question.getQuestionText());
        //display back n forward buttons
        //display question counter
        questionCounter.setText((questionIndex+1) + "/" +(survey.getQuestionList().size()) + ".");

        Question answeredQ = null;
        for(Question q: answeredQuestions)
        {
            if(q.getQuestionText().equals(question.getQuestionText()))
            {
                answeredQ = q;
            }
        }

        if(isMultipleChoice)
        {
            //display multiple choice stuff
            radioGroup.setVisibility(View.VISIBLE);
            answer.setVisibility(View.GONE);

            if(answeredQ != null)
            {
                radioGroup.check(answeredQ.getAnswerNumber());
            }
        }
        else
        {
            //display text box for answer
            radioGroup.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.VISIBLE);

            if(answeredQ != null)
            {
                answer.setText(answeredQ.getAnswerString());
            }
        }
    }

    private Survey getSurvey(String surveyString) throws IOException, JSONException
    {

        Survey survey = new Survey();

        StringBuilder builder = new StringBuilder();

        InputStream inputStream;
        inputStream = getResources().openRawResource(R.raw.surveys);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null)
        {
            builder.append(line);
        }

        String json = builder.toString();

        JSONObject data = new JSONObject(json);

        JSONArray surveysJsonObject = data.getJSONArray("survey");

        for (int i = 0; i < surveysJsonObject.length(); i++)
        {
            JSONObject surveyJsonObject = surveysJsonObject.getJSONObject(i);

            String surveyTitle = surveyJsonObject.getString("title");

            if (!surveyTitle.equals(surveyString))
            {
                continue;
            }

            JSONArray questions = surveyJsonObject.getJSONArray("questions");

            List<Question> questionList = new ArrayList<>();
            for (int j = 0; j < questions.length(); j++)
            {
                String questionString = questions.getJSONObject(j).getString("question");
                boolean isMultipleChoice = true;
                if (questionString.endsWith("..."))
                {
                    isMultipleChoice = false;
                }
                Question question = new Question(questionString, isMultipleChoice, null, null);

                questionList.add(question);

            }

            survey.setTitle(surveyTitle);
            survey.setQuestionList(questionList);
        }
        return survey;
    }

    @Override
    public void onBackPressed()
    {
        if(surveyInProgress)
        {
            back.callOnClick();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
