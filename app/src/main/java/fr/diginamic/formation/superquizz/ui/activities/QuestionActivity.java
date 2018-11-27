package fr.diginamic.formation.superquizz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fr.diginamic.formation.superquizz.R;
import fr.diginamic.formation.superquizz.database.QuestionsDatabaseHelper;
import fr.diginamic.formation.superquizz.model.Question;
import fr.diginamic.formation.superquizz.ui.thread.QuestionTask;

public class QuestionActivity extends AppCompatActivity implements QuestionTask.QuestionTaskListener{
    private final String INDEX = "index";
    private final String SCORE = "score";
    private ArrayList<Question> questionsList;
    private Question question;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private int index = 0;
    private int size = 0;
    private int score = 0;
    private QuestionTask questionTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initActivity(savedInstanceState);

    }

    public  void initActivity(Bundle savedInstanceState){
        if(savedInstanceState != null){
            index = savedInstanceState.getInt(INDEX);
            score = savedInstanceState.getInt(SCORE);
        }
        questionsList = QuestionsDatabaseHelper.getInstance(this).getAllQuestions();
        size = questionsList.size();
        question = questionsList.get(index);

        answer1 = findViewById(R.id.answer_1);
        answer2 = findViewById(R.id.answer_2);
        answer3 = findViewById(R.id.answer_3);
        answer4 = findViewById(R.id.answer_4);

        loadContentQuestion();

        answer1.setOnClickListener(v -> verifyAnswer(question, answer1.getText().toString(), v));
        answer2.setOnClickListener(v -> verifyAnswer(question, answer2.getText().toString(), v));
        answer3.setOnClickListener(v -> verifyAnswer(question, answer3.getText().toString(), v));
        answer4.setOnClickListener(v -> verifyAnswer(question, answer4.getText().toString(), v));
    }

    private void verifyAnswer(Question question, String answer, View button){
        questionTask.cancel(true);

        if (question.getGoodAnswer().equals(answer)) {
            score += question.getPoint();
        }
        if (index == questionsList.size() - 1) {
            Intent resultIntent = new Intent(this, ResultActivity.class);
            resultIntent.putExtra(ResultActivity.SCORE, score);
            startActivity(resultIntent);
        } else {
            index++;
            loadContentQuestion();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        questionTask.cancel(true);
    }

    public void loadContentQuestion(){

        int current = index + 1;
        setTitle("Question " + current + "/" + size);

        question = questionsList.get(index);


        ((TextView) findViewById(R.id.question)).setText(question.getEntitle());

        answer1.setText(question.getProposition(0));
        answer2.setText(question.getProposition(1));
        answer3.setText(question.getProposition(2));
        answer4.setText(question.getProposition(3));

        questionTask = new QuestionTask(this);
        questionTask.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INDEX, index);
        outState.putInt(SCORE, score);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProgressQuestionTask(int count) {
       ((ProgressBar) findViewById(R.id.progress_bar)).setProgress(100 - count);

       if(count == 100){
           if (index == questionsList.size() - 1) {
               Intent resultIntent = new Intent(this, ResultActivity.class);
               resultIntent.putExtra(ResultActivity.SCORE, score);
               startActivity(resultIntent);
           } else {
               index++;
               loadContentQuestion();
           }
       }
    }
}
