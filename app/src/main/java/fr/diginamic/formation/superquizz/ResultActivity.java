package fr.diginamic.formation.superquizz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    public static String SCORE = "score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        QuestionMemDao questionMemDao = new QuestionMemDao();

        int score = getIntent().getIntExtra(this.SCORE, 0);

        ((TextView) findViewById(R.id.result_score_view)).setText(score + "/" + questionMemDao.getMaxPoint());

    }

}
