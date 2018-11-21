package fr.diginamic.formation.superquizz.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fr.diginamic.formation.superquizz.R;
import fr.diginamic.formation.superquizz.model.Question;

public class QuestionActivity extends AppCompatActivity {
    public static final String QUESTION = "question";
    public static final String FROM_LIST = "from_list";
    private final String INDEX = "index";
    private final String SCORE = "score";
    private ArrayList<Question> listQuestions;
    private Question question;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private boolean fromList = false;
    private int index = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fromList = getIntent().getBooleanExtra(FROM_LIST, false);
        question = getIntent().getParcelableExtra(QUESTION);

        initActivity(savedInstanceState);

    }

    private void verifyAnswer(Question question, int answer, View button){
        if(fromList){
            resetColor();
            if(question.getBonneReponse() == answer){
                button.setBackgroundColor(Color.GREEN);
            }else{
                button.setBackgroundColor(Color.RED);
            }

        }else{
            if(question.getBonneReponse() == answer){
                score += question.getPoint();
            }
            if (index == listQuestions.size() - 1){
                Intent resultIntent = new Intent(this, ResultActivity.class);
                resultIntent.putExtra(ResultActivity.SCORE, score);
                startActivity(resultIntent);
            }else {
                index++;
                loadContentQuestion();
            }
        }
    }

    public  void initActivity(Bundle savedInstanceState){
        if(savedInstanceState != null){
            fromList = savedInstanceState.getBoolean(FROM_LIST);
            if(!fromList){
                index = savedInstanceState.getInt(INDEX);
                score = savedInstanceState.getInt(SCORE);
            }
        }

        if(!fromList){
            listQuestions = MainActivity.listQuestions;
            question = listQuestions.get(index);
        }
        answer1 = findViewById(R.id.answer_1);
        answer2 = findViewById(R.id.answer_2);
        answer3 = findViewById(R.id.answer_3);
        answer4 = findViewById(R.id.answer_4);

        loadContentQuestion();

        answer1.setOnClickListener(v -> verifyAnswer(question, 1, v));
        answer2.setOnClickListener(v -> verifyAnswer(question, 2, v));
        answer3.setOnClickListener(v -> verifyAnswer(question, 3, v));
        answer4.setOnClickListener(v -> verifyAnswer(question, 4, v));
    }

    public void loadContentQuestion(){
        if (!fromList){
            question = listQuestions.get(index);
        }

        ((TextView) findViewById(R.id.question)).setText(question.getIntitule());

        answer1.setText(question.getProposition(0));
        answer2.setText(question.getProposition(1));
        answer3.setText(question.getProposition(2));
        answer4.setText(question.getProposition(3));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
         if(fromList){
            outState.putParcelable(QUESTION, question);
        }else {
            outState.putInt(INDEX, index);
            outState.putInt(SCORE, score);
        }
        outState.putBoolean(FROM_LIST, fromList);

        super.onSaveInstanceState(outState);
    }

    public void resetColor(){
        answer1.setBackgroundColor(getColor(R.color.colorAccent));
        answer2.setBackgroundColor(getColor(R.color.colorAccent));
        answer3.setBackgroundColor(getColor(R.color.colorAccent));
        answer4.setBackgroundColor(getColor(R.color.colorAccent));
    }
}
