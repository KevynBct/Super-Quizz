package fr.diginamic.formation.superquizz.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import fr.diginamic.formation.superquizz.dao.QuestionMemDao;
import fr.diginamic.formation.superquizz.R;

public class ResultActivity extends AppCompatActivity {
    public static String SCORE = "score";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        QuestionMemDao questionMemDao = new QuestionMemDao();

        int score = getIntent().getIntExtra(SCORE, 0);

        ((TextView) findViewById(R.id.result_score_view)).setText(score + "/" + questionMemDao.getMaxPoint());

    }

}
