package fr.diginamic.formation.superquizz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import fr.diginamic.formation.superquizz.R;
import fr.diginamic.formation.superquizz.ui.fragments.PlayFragment;
import fr.diginamic.formation.superquizz.ui.fragments.ScoreFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlayFragment.OnFragmentInteractionListener, ScoreFragment.OnFragmentInteractionListener {
    private final String CURRENT_FRAGMENT = "current_fragment";
    private int idFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initActivity(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_play) {
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            PlayFragment fragment = new PlayFragment();
            fragmentTransaction.replace(R.id.fragmentLayout, fragment);
            fragmentTransaction.commit();
            idFragment = 0;
        } else if (id == R.id.nav_add) {

        } else if (id == R.id.nav_list) {

        } else if (id == R.id.nav_delete) {
            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_score) {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new ScoreFragment()).commit();
            idFragment = 4;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                case 4 : {
                    this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, new ScoreFragment()).commit();
                    idFragment = 4;
                    break;
                }
                default: {
                    this.getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, new PlayFragment()).commit();
                    break;
                }
            }

        }else {
            this.getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, new PlayFragment()).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}