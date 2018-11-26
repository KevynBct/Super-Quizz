package fr.diginamic.formation.superquizz.ui.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import fr.diginamic.formation.superquizz.R;
import fr.diginamic.formation.superquizz.broadcast.NetworkChangeReceiver;
import fr.diginamic.formation.superquizz.database.QuestionsDatabaseHelper;
import fr.diginamic.formation.superquizz.model.Question;
import fr.diginamic.formation.superquizz.ui.fragments.AddQuestionFragment;
import fr.diginamic.formation.superquizz.ui.fragments.PlayFragment;
import fr.diginamic.formation.superquizz.ui.fragments.QuestionListFragment;
import fr.diginamic.formation.superquizz.ui.fragments.ScoreFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlayFragment.PlayFragmentListener, QuestionListFragment.QuestionListListener, AddQuestionFragment.AddQuestionListener {
    private final String CURRENT_FRAGMENT = "current_fragment";
    private int idFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        QuestionsDatabaseHelper.getInstance(this).getAllQuestions();

        initActivity(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(idFragment == 0) {
                super.onBackPressed();
            }else if(idFragment == 1){
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new QuestionListFragment()).commit();
                idFragment = 2;
            }else{
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new PlayFragment()).commit();
                idFragment = 0;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_play) {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new PlayFragment()).commit();
            idFragment = 0;
        } else if (id == R.id.nav_add) {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new AddQuestionFragment()).commit();
            idFragment = 1;
        } else if (id == R.id.nav_list) {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new QuestionListFragment(), "FRAGMENT_LIST_TAG").commit();
            idFragment = 2;
        } else if (id == R.id.nav_score) {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new ScoreFragment()).commit();
            idFragment = 4;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT, idFragment);
    }

    public void initActivity(Bundle savedInstanceState){
        if(savedInstanceState != null){
            switch (savedInstanceState.getInt(CURRENT_FRAGMENT)){
                case 0 : {
                    this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new PlayFragment()).commit();
                    idFragment = 0;
                    break;
                }
                case 1 : {
                    this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new AddQuestionFragment()).commit();
                    idFragment = 1;
                    break;
                }
                case 2 : {
                    this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new QuestionListFragment()).commit();
                    idFragment = 2;
                    break;
                }
                case 4 : {
                    this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new ScoreFragment()).commit();
                    idFragment = 4;
                    break;
                }
                default: {
                    this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new PlayFragment()).commit();
                    break;
                }
            }
        }else {
            this.getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, new PlayFragment()).commit();
        }
    }


    @Override
    public void onListFragmentInteraction(Question question) {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, AddQuestionFragment.newInstance(question, true)).commit();
        idFragment = 1;
    }

    @Override
    public void onLongClickQuestion(Question question) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Voulez vous supprimer cette question ?")
                .setTitle("Suppression");
        builder.setPositiveButton("Oui", (dialog1, which) -> {
            QuestionsDatabaseHelper.getInstance(this).deleteQuestion(question);
            updateQuestionsListFragment();
            Toast.makeText(this, "La question "+ question.getEntitle() + " est supprimée", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Non", (dialog1, which) -> Log.i("DIALOG", "Annuler"));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void updateQuestionsListFragment() {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, QuestionListFragment.newInstance(1)).commit();
    }

    @Override
    public void saveQuestion(Question question) {
        QuestionsDatabaseHelper.getInstance(this).addQuestion(question, true);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, QuestionListFragment.newInstance(1)).commit();
    }

    @Override
    public void editQuestion(Question question, int id) {
        QuestionsDatabaseHelper.getInstance(this).updateQuestion(question, id);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, QuestionListFragment.newInstance(1)).commit();
    }


    @Override
    public void onPlayButton() {
        if(QuestionsDatabaseHelper.getInstance(this).getAllQuestions().isEmpty()){
            Toast.makeText(this, "Il n'y a aucune question", Toast.LENGTH_SHORT).show();
        }else {
            Intent questionIntent = new Intent(getApplicationContext(), QuestionActivity.class);
            startActivity(questionIntent);
        }
    }
}
