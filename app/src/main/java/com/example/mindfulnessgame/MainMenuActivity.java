package com.example.mindfulnessgame;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.Collections;

public class MainMenuActivity extends AppCompatActivity {

    static final String ENDLESS_MODE = "endlessMode";

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    static boolean buttonsSoundEffect;
    int levelToUnlockIM = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);

        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();

        buttonsSoundEffect = preferences.getBoolean(SettingsActivity.BUTTONS_SOUND_EFFECT, true);

        changeColors();

        SettingsActivity.fillImages();

        showStateOfEndlessMode();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        buttonsSoundEffect = preferences.getBoolean(SettingsActivity.BUTTONS_SOUND_EFFECT, true);

        changeColors();

        showStateOfEndlessMode();
    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonsSoundEffect = preferences.getBoolean(SettingsActivity.BUTTONS_SOUND_EFFECT, true);

        changeColors();

        showStateOfEndlessMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonsSoundEffect = preferences.getBoolean(SettingsActivity.BUTTONS_SOUND_EFFECT, true);

        changeColors();

        showStateOfEndlessMode();
    }

    public void showStateOfEndlessMode() {
        ImageButton endlessModeIB = findViewById(R.id.endless_mode_IB);
        TextView endlessModeTV = findViewById(R.id.endless_mode_TV);
        if (preferences.getInt(LevelsActivity.CURRENT_UNLOCKED_LEVEL, 0) < levelToUnlockIM) {
            endlessModeIB.setImageResource(R.drawable.white_cross);
            endlessModeTV.setTextColor(Color.parseColor("#9e9e9e"));
        } else {
            endlessModeIB.setImageResource(0);
            endlessModeTV.setTextColor(Color.parseColor("#d9d9d9"));
            showHighScore();
        }
    }

    public void showHighScore() {
        TextView highScoreTV = findViewById(R.id.high_score_TV);
        int highScore = preferences.getInt(MainActivity.HIGH_SCORE, 0);

        highScoreTV.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_shape));
        String out = "РЕКОРД: " + highScore + " ";
        if (highScore % 10 == 0 || highScore % 10 > 4) out += "УРОВНЕЙ";
        else if (highScore % 10 > 1 && highScore % 10 < 5) out += "УРОВНЯ";
        else out += "УРОВЕНЬ";
        highScoreTV.setText(out);
    }

    public void changeColors() {
        // у вас появились вопросы: зачем, почему, для чего? у меня самого они появились, когда я этот блок кода писал
        // ответов я так и не нашёл, но отображение элементов разметки по-другому корректно не работает
        GradientDrawable background = (GradientDrawable) findViewById(R.id.main_menu_CL).getBackground();
        background.setColor(Color.parseColor(preferences.getString(SettingsActivity.BACKGROUND_COLOR, "#f01ff0")));

        GradientDrawable roundedShape = (GradientDrawable) findViewById(R.id.mindfulness_game_TV).getBackground();
        roundedShape.setColor(Color.parseColor(preferences.getString(SettingsActivity.TEXT_BACKGROUND_COLOR, "#88008c")));

        roundedShape = (GradientDrawable) findViewById(R.id.levels_IB).getBackground();
        roundedShape.setColor(Color.parseColor(preferences.getString(SettingsActivity.TEXT_BACKGROUND_COLOR, "#88008c")));

        findViewById(R.id.endless_mode_IB).setBackground(roundedShape);

        roundedShape = (GradientDrawable) findViewById(R.id.settings_IB).getBackground();
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
        if (preferences.getInt(LevelsActivity.CURRENT_UNLOCKED_LEVEL, 0) < levelToUnlockIM) {
            Toast.makeText(this, "ПРОЙДИТЕ УРОВЕНЬ " + levelToUnlockIM + ",\nЧТОБЫ РАЗБЛОКИРОВАТЬ", Toast.LENGTH_SHORT).show();
            return;
        }

        playClickSound(this);

        showHighScore();

        Intent mainActivity = new Intent(this, MainActivity.class);
        mainActivity.putExtra(LevelsActivity.LEVEL, new Level(2000, 2000,
                new ArrayList<>(Collections.singletonList(SettingsActivity.images.get((int) (Math.random() * SettingsActivity.images.size())))),
                1, 0));
        mainActivity.putExtra(ENDLESS_MODE, true);
        startActivity(mainActivity);
    }

    public static void playClickSound(Context context) {
        if (buttonsSoundEffect) MediaPlayer.create(context, R.raw.bubble_click).start();
    }
}