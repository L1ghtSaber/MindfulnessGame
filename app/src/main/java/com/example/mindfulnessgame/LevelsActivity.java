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

    @Override
    protected void onRestart() {
        super.onRestart();
        showBlockedLevels();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showBlockedLevels();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

        Random r = new Random();
        Level[] levels = new Level[]{
                new Level(1000, 1000, new int[][]{allowedImages.get(r.nextInt(allowedImages.size()))}, 5, 0),

        };

        main.putExtra(LEVEL_KEY, levels[chosenLevel]);
        startActivity(main);
    }

    public void exitToMainMenu(View view) {
        finish();
    }
}