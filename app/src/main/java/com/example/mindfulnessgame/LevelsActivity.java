package com.example.mindfulnessgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LevelsActivity extends AppCompatActivity {

    static final String LEVEL_KEY = "level";

    Intent main;
    int[] levelButtonIds = new int[] {
            R.id.level_1_IB, R.id.level_2_IB,
            R.id.level_3_IB, R.id.level_4_IB,
            R.id.level_5_IB, R.id.level_6_IB,
            R.id.level_7_IB, R.id.level_8_IB,
            R.id.level_9_IB, R.id.level_10_IB
    };
    int[] levelNumberIds = new int[] {
            R.id.level_1_TV, R.id.level_2_TV,
            R.id.level_3_TV, R.id.level_4_TV,
            R.id.level_5_TV, R.id.level_6_TV,
            R.id.level_7_TV, R.id.level_8_TV,
            R.id.level_9_TV, R.id.level_10_TV
    };
    ImageButton[] levelButtons = new ImageButton[levelButtonIds.length];

    int chosenLevel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_levels);

        main = new Intent(LevelsActivity.this, MainActivity.class);

        for (int i = 0; i < levelButtons.length; i++) levelButtons[i] = findViewById(levelButtonIds[i]);

        for (int i = 0; i < levelButtons.length; i++) {
            final int i1 = i;
            levelButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainMenuActivity.playClickSound(LevelsActivity.this);

                    if (chosenLevel != i1) {
                        chosenLevel = i1;
                        selectLevel();
                    } else {
                        levelButtons[i1].setImageResource(0);
                        chosenLevel = -1;
                    }
                }
            });
        }
        showBlockedLevels();
    }

    public void selectLevel() {
        for (int i = 0; i < levelButtons.length; i++) {
            if (i == chosenLevel) {
                levelButtons[i].setImageResource(R.drawable.selection);
            } else levelButtons[i].setImageResource(0);
        }
        showBlockedLevels();
    }

    public void showBlockedLevels() {
        for (int i = levelButtons.length - 1; i > MainMenuActivity.preferences.getInt(MainMenuActivity.CURRENT_UNLOCKED_LEVEL_KEY, 0); i--) {
            levelButtons[i].setImageResource(R.drawable.cross);
            levelButtons[i].setClickable(false);
            ((TextView) findViewById(levelNumberIds[i])).setTextColor(getResources().getColor(R.color.light_gray));
        }
    }

    public void chooseLevel(View view) {
        MainMenuActivity.playClickSound(this);

        if (chosenLevel == -1) return;

        Level[] levels = new Level[10];
        ArrayList<int[]> images = new ArrayList<>();
        images.add(SettingsActivity.images.get((int) (Math.random() * SettingsActivity.images.size())));
        for (int i = 0, imageTime = 1000, switchTime = 1000, imagesAmount = 3; i < levels.length; i++) {
            levels[i] = new Level(imageTime, switchTime, images, imagesAmount, i);

            imageTime -= 100;
            if ((i + 1) % 2 == 0) switchTime -= 200;
            if (i == 0 || i == 2 || i == 6) imagesAmount += 2;
            else if (i == 4 || i == 8) imagesAmount += 3;
            if ((i + 1) % 3 == 0) images.add(SettingsActivity.images.get((int) (Math.random() * SettingsActivity.images.size())));

            if (imageTime <= 0) imageTime = 10;
            if (switchTime <= 0) switchTime = 10;
        }
        main.putExtra(LEVEL_KEY, levels[chosenLevel]);
        main.putExtra(MainMenuActivity.INFINITE_MODE, false);
        startActivity(main);
        finish();
    }

    public void exitToMainMenu(View view) {
        MainMenuActivity.playClickSound(this);
        finish();
    }
}