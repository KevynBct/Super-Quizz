package fr.diginamic.formation.superquizz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    public static String INDEX = "index_question";
    public static String SCORE = "score";
    private ArrayList<Question> listeQuestions;
    Button answer1 = findViewById(R.id.answer_1);
    Button answer2 = findViewById(R.id.answer_2);
    Button answer3 = findViewById(R.id.answer_3);
    Button answer4 = findViewById(R.id.answer_4);
    private int index;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        index = 0;
        score = 0;
        QuestionMemDao questionMemDao = new QuestionMemDao();

        listeQuestions = questionMemDao.findAll();

        if(getIntent() != null){
            index = getIntent().getIntExtra(this.INDEX, 0);
            score = getIntent().getIntExtra(this.SCORE, 0);
        }

        Question question = listeQuestions.get(index);

        ((TextView) findViewById(R.id.question)).setText(question.getIntitule());

        Button answer1 = findViewById(R.id.answer_1);
        Button answer2 = findViewById(R.id.answer_2);
        Button answer3 = findViewById(R.id.answer_3);
        Button answer4 = findViewById(R.id.answer_4);

        answer1.setText(question.getProposition(0));
        answer2.setText(question.getProposition(1));
        answer3.setText(question.getProposition(2));
        answer4.setText(question.getProposition(3));

        answer1.setOnClickListener(v -> verifyAnswer(question, 1));
        answer2.setOnClickListener(v -> verifyAnswer(question, 2));
        answer3.setOnClickListener(v -> verifyAnswer(question, 3));
        answer4.setOnClickListener(v -> verifyAnswer(question, 4));
    }

    private void verifyAnswer(Question question, int answer){
        Intent resultIntent;
        if (index == listeQuestions.size() - 1){
            resultIntent = new Intent(this, ResultActivity.class);
        }else{
            resultIntent = new Intent(this, QuestionActivity.class);
            resultIntent.putExtra(QuestionActivity.INDEX, index+1);
        }
        if(question.getBonneReponse() == answer){
            resultIntent.putExtra(QuestionActivity.SCORE, score + question.getPoint());
        }else{
            resultIntent.putExtra(QuestionActivity.SCORE, score);
        }
        startActivity(resultIntent);
    }
    
}
