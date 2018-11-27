package fr.diginamic.formation.superquizz.ui.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

import fr.diginamic.formation.superquizz.R;

public class PreferencesActivity extends AppCompatActivity {
    public static final String SAVE_ONLINE = "saveOnline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Switch saveOnline = findViewById(R.id.switch_preferences);

        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mSettings.edit();

        saveOnline.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean(SAVE_ONLINE, isChecked);
            editor.apply();
        });

        saveOnline.setChecked(mSettings.getBoolean(SAVE_ONLINE, true));
    }
}
