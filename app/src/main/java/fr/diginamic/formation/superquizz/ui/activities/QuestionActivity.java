package fr.diginamic.formation.superquizz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import fr.diginamic.formation.superquizz.dao.QuestionMemDao;
import fr.diginamic.formation.superquizz.R;
import fr.diginamic.formation.superquizz.model.Question;

public class QuestionActivity extends AppCompatActivity {
    private final String INDEX = "index";
    private final String SCORE = "score";
    private ArrayList<Question> listeQuestions;
    private Question question;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private int index;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initActivity(savedInstanceState);

    }

    private void verifyAnswer(Question question, int answer){
        if(question.getBonneReponse() == answer){
            score += question.getPoint();
        }
        if (index == listeQuestions.size() - 1){
            Intent resultIntent = new Intent(this, ResultActivity.class);
            resultIntent.putExtra(ResultActivity.SCORE, score);
            startActivity(resultIntent);
        }else {
            index++;
            loadContentQuestion();
        }
    }

    public  void initActivity(Bundle savedInstanceState){
        if(savedInstanceState != null){
            index = savedInstanceState.getInt(INDEX);
            score = savedInstanceState.getInt(SCORE);
        }else{
            index = 0;
            score = 0;
        }
        QuestionMemDao questionMemDao = new QuestionMemDao();

        listeQuestions = questionMemDao.findAll();

        question = listeQuestions.get(index);

        answer1 = findViewById(R.id.answer_1);
        answer2 = findViewById(R.id.answer_2);
        answer3 = findViewById(R.id.answer_3);
        answer4 = findViewById(R.id.answer_4);

        loadContentQuestion();

        answer1.setOnClickListener(v -> verifyAnswer(question, 1));
        answer2.setOnClickListener(v -> verifyAnswer(question, 2));
        answer3.setOnClickListener(v -> verifyAnswer(question, 3));
        answer4.setOnClickListener(v -> verifyAnswer(question, 4));
    }

    public void loadContentQuestion(){
        question = listeQuestions.get(index);

        ((TextView) findViewById(R.id.question)).setText(question.getIntitule());

        answer1.setText(question.getProposition(0));
        answer2.setText(question.getProposition(1));
        answer3.setText(question.getProposition(2));
        answer4.setText(question.getProposition(3));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INDEX, index);
        outState.putInt(SCORE, score);
        super.onSaveInstanceState(outState);
    }
}
