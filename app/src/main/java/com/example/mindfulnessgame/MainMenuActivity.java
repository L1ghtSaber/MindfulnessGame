package com.example.mindfulnessgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainMenuActivity extends AppCompatActivity {

    static final String CURRENT_UNLOCKED_LEVEL_KEY = "currentUnlockedLevel";
    static final String INFINITE_MODE = "infiniteMode";

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);

        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();

        changeColors();

        SettingsActivity.fillImages();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        changeColors();
    }

    public void changeColors() {
        GradientDrawable background = (GradientDrawable) ((ConstraintLayout) findViewById(R.id.main_menu_CL)).getBackground();
        background.setColor(Color.parseColor(preferences.getString(SettingsActivity.BACKGROUND_COLOR, "#f01ff0")));

        GradientDrawable roundedShape = (GradientDrawable) ((TextView) findViewById(R.id.mindfulness_game_TV)).getBackground();
        roundedShape.setColor(Color.parseColor(preferences.getString(SettingsActivity.TEXT_BACKGROUND_COLOR, "#88008c")));

        roundedShape = (GradientDrawable) ((ImageButton) findViewById(R.id.levels_IB)).getBackground();
        roundedShape.setColor(Color.parseColor(preferences.getString(SettingsActivity.TEXT_BACKGROUND_COLOR, "#88008c")));

        ((ImageButton) findViewById(R.id.infinite_mode_IB)).setBackground(roundedShape);

        roundedShape = (GradientDrawable) ((ImageButton) findViewById(R.id.settings_IB)).getBackground();
        roundedShape.setColor(Color.parseColor(preferences.getString(SettingsActivity.TEXT_BACKGROUND_COLOR, "#88008c")));
    }

    public void openLevels(View view) {
        playClickSound(this);

        startActivity(new Intent(this, LevelsActivity.class));
    }

    public void openSettings(View view) {
        playClickSound(this);

        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void startInfiniteGame(View view) {
        playClickSound(this);

        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.putExtra(LevelsActivity.LEVEL_KEY, new Level(2000, 2000,
                new ArrayList<>(Collections.singletonList(SettingsActivity.images.get((int) (Math.random() * SettingsActivity.images.size())))),
                1, 0));
        mainActivity.putExtra(INFINITE_MODE, true);
        startActivity(mainActivity);
    }

    public static void playClickSound(Context context) {
        MediaPlayer.create(context, R.raw.bubble_click).start();
    }
}