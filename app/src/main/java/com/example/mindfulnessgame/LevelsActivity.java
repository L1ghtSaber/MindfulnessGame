package com.example.mindfulnessgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class LevelsActivity extends AppCompatActivity {

    static final String LEVEL_KEY = "level";

    Intent main;
    int[] levelButtonIds = new int[] {
            R.id.level_button1, R.id.level_button2,
            R.id.level_button3, R.id.level_button4,
            R.id.level_button5, R.id.level_button6,
            R.id.level_button7, R.id.level_button8,
            R.id.level_button9, R.id.level_button10
    };
    int[] levelNumberIds = new int[] {
            R.id.level_number1_TV, R.id.level_number2_TV,
            R.id.level_number3_TV, R.id.level_number4_TV,
            R.id.level_number5_TV, R.id.level_number6_TV,
            R.id.level_number7_TV, R.id.level_number8_TV,
            R.id.level_number9_TV, R.id.level_number10_TV
    };
    ImageButton[] levelButtons = new ImageButton[levelButtonIds.length];

    ArrayList<int[]> allowedImages = new ArrayList<>();

    int chosenLevel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_levels);

        main = new Intent(LevelsActivity.this, MainActivity.class);
        int currentUnlockedLevel = MainMenuActivity.preferences.getInt(MainMenuActivity.CURRENT_UNLOCKED_LEVEL_KEY, 0);

        for (int i = 0; i < levelButtons.length; i++) levelButtons[i] = findViewById(levelButtonIds[i]);

        for (int i = 0; i < levelButtons.length; i++) {
            final int i1 = i;
            levelButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i1 <= currentUnlockedLevel) {
                        if (chosenLevel != i1) {
                            chosenLevel = i1;
                            selectLevel();
                        }
                        else {
                            levelButtons[i1].setImageResource(0);
                            chosenLevel = -1;
                        }
                    }
                }
            });
        }

        String groupNames = MainMenuActivity.preferences.getString(SettingsActivity.ALLOWED_IMAGES, "");
        String currentName = "";
        for (int i = 0; i < groupNames.length(); i++) {
            if (groupNames.charAt(i) != '|') currentName += groupNames.charAt(i);
            else {
                allowedImages.add(SettingsActivity.images.get(currentName));
                currentName = "";
            }
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
            ((TextView) findViewById(levelNumberIds[i])).setTextColor(getResources().getColor(R.color.light_gray));
        }
    }

    public void chooseLevel(View view) {
        if (chosenLevel == -1) return;

        Level[] levels = new Level[10];
        ArrayList<int[]> images = new ArrayList<>();
        images.add(allowedImages.get((int) (Math.random() * allowedImages.size())));
        for (int i = 0, imageTime = 1000, switchTime = 1000, imagesAmount = 3; i < levels.length; i++) {
            levels[i] = new Level(imageTime, switchTime, images, imagesAmount, i);

            imageTime -= 100;
            if ((i + 1) % 2 == 0) switchTime -= 200;
            if (i == 0 || i == 2 || i == 6) imagesAmount += 2;
            else if (i == 4 || i == 8) imagesAmount += 3;
            if ((i + 1) % 3 == 0) images.add(allowedImages.get((int) (Math.random() * allowedImages.size())));

            if (imageTime <= 0) imageTime = 10;
            if (switchTime <= 0) switchTime = 10;
        }
        main.putExtra(LEVEL_KEY, levels[chosenLevel]);
        startActivity(main);
        finish();
    }

    public void exitToMainMenu(View view) {
        finish();
    }
}