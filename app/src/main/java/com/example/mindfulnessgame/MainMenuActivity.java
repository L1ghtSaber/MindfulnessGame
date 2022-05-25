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

public class MainMenuActivity extends AppCompatActivity {

    static final String CURRENT_UNLOCKED_LEVEL_KEY = "currentUnlockedLevel";

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

        roundedShape = (GradientDrawable) ((ImageButton) findViewById(R.id.settings_IB)).getBackground();
        roundedShape.setColor(Color.parseColor(preferences.getString(SettingsActivity.TEXT_BACKGROUND_COLOR, "#88008c")));
    }

    public void levels(View view) {
        startActivity(new Intent(this, LevelsActivity.class));
        playClickSound(this);
    }

    public void openSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
        playClickSound(this);
    }

    public static void playClickSound(Context context) {
        MediaPlayer.create(context, R.raw.bubble_click).start();
    }
}