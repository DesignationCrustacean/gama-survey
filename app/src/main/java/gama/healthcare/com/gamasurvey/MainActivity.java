package gama.healthcare.com.gamasurvey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    Button trainingReaction, behaviour, outcomes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainingReaction = (Button) findViewById(R.id.bt_trainingSurvey);
        behaviour = (Button) findViewById(R.id.bt_behaviourSurvey);
        outcomes = (Button) findViewById(R.id.bt_outcomesSurvey);

        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button button = (Button) v;

                Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
                intent.putExtra("survey", button.getText());
                startActivity(intent);
                finish();

            }
        };

        trainingReaction.setOnClickListener(listener);
        behaviour.setOnClickListener(listener);
        outcomes.setOnClickListener(listener);
    }





}
