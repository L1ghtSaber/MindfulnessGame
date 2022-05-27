package com.example.mindfulnessgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LevelsActivity extends AppCompatActivity {

    static final String CURRENT_UNLOCKED_LEVEL = "currentUnlockedLevel";
    static final String LEVEL = "level";

    Intent main;
    int[] levelButtonIds = new int[] {
            R.id.level_1_IB, R.id.level_2_IB,
            R.id.level_3_IB, R.id.level_4_IB,
            R.id.level_5_IB, R.id.level_6_IB,
            R.id.level_7_IB, R.id.level_8_IB,
            R.id.level_9_IB, R.id.level_10_IB
    };
    ImageButton[] levelButtons = new ImageButton[levelButtonIds.length];
    int[] levelNumberIds = new int[] {
            R.id.level_1_TV, R.id.level_2_TV,
            R.id.level_3_TV, R.id.level_4_TV,
            R.id.level_5_TV, R.id.level_6_TV,
            R.id.level_7_TV, R.id.level_8_TV,
            R.id.level_9_TV, R.id.level_10_TV
    };

    int chosenLevel = -1, currentTenLevels = 1, maxTenLevels = 3;

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

                    if (chosenLevel != i1) chosenLevel = i1;
                    else {
                        levelButtons[i1].setImageResource(0);
                        chosenLevel = -1;
                    }

                    showBlockedLevels();
                }
            });
        }
        setLevelNumbers();

        showBlockedLevels();
    }

    public void setLevelNumbers() {
        for (int i = 0; i < levelNumberIds.length; i++) {
            String out = "" + (i + 1 + (currentTenLevels - 1) * 10);
            ((TextView) findViewById(levelNumberIds[i])).setText(out);
        }
    }

    public void showBlockedLevels() {
        int currentUnlockedLevel = MainMenuActivity.preferences.getInt(CURRENT_UNLOCKED_LEVEL, 0);
        for (int i = 0; i < levelButtons.length; i++) {
            TextView levelNumberTV = findViewById(levelNumberIds[i]);

            int levelNumber = Integer.parseInt(levelNumberTV.getText().toString());
            if (levelNumber <= currentUnlockedLevel) {
                levelButtons[i].setImageResource(0);
                if (i == chosenLevel) levelButtons[i].setImageResource(R.drawable.selection);
                levelButtons[i].setClickable(true);
                levelNumberTV.setTextColor(Color.parseColor("#d9d9d9"));
                continue;
            }

            levelButtons[i].setImageResource(R.drawable.cross);
            levelButtons[i].setClickable(false);
            levelNumberTV.setTextColor(Color.parseColor("#9e9e9e"));
        }
    }

    public void chooseLevel(View view) {
        MainMenuActivity.playClickSound(this);

        if (chosenLevel == -1) return;

        Level[] levels = new Level[10];
        ArrayList<int[]> images = new ArrayList<>();
        for (int i = 0; i < currentTenLevels; i++)
            images.add(SettingsActivity.images.get((int) (Math.random() * SettingsActivity.images.size())));
        for (int i = 0, imageTime = 1250 / currentTenLevels, switchTime = 1500 / currentTenLevels,
             imagesAmount = 3 * currentTenLevels; i < levels.length; i++) {

            levels[i] = new Level(imageTime, switchTime, images, imagesAmount, i + (currentTenLevels - 1) * 10);

            imageTime -= 10 * currentTenLevels;
            if (imageTime <= 0) imageTime = 5;

            if ((i + 1) % 2 == 0) switchTime -= 20 * currentTenLevels;
            if (switchTime <= 0) switchTime = 5;


            if (i == 0 || i == 2 || i == 6) imagesAmount += 2;
            else if (i == 4 || i == 8) imagesAmount += 3;

            if ((i + 1) % 5 == 0) {
                for (int j = 0; j < currentTenLevels; j++)
                    images.add(SettingsActivity.images.get((int) (Math.random() * SettingsActivity.images.size())));
            }
        }
        main.putExtra(LEVEL, levels[chosenLevel]);
        main.putExtra(MainMenuActivity.ENDLESS_MODE, false);
        startActivity(main);
        finish();
    }

    public void exitToMainMenu(View view) {
        MainMenuActivity.playClickSound(this);
        finish();
    }

    public void changeCurrentTenLevels(View view) {
        MainMenuActivity.playClickSound(this);

        if (view.getId() == R.id.previous_ten_levels_IB) currentTenLevels--;
        else if (view.getId() == R.id.next_ten_levels_IB) currentTenLevels++;

        if (currentTenLevels < 1) currentTenLevels = maxTenLevels;
        else if (currentTenLevels > maxTenLevels) currentTenLevels = 1;

        chosenLevel = -1;

        setLevelNumbers();
        showBlockedLevels();
    }
}