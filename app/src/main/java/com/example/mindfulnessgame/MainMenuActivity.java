package com.example.mindfulnessgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainMenuActivity extends AppCompatActivity {

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    static final String CURRENT_UNLOCKED_LEVEL_KEY = "currentUnlockedLevel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);
        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();
        SettingsActivity.fillImages();
    }

    public void levels(View view) {
        startActivity(new Intent(MainMenuActivity.this, LevelsActivity.class));
    }
}