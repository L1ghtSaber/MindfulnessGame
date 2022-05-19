package com.example.mindfulnessgame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

public class LevelsActivity extends AppCompatActivity {

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
    int currentUnlockedLevel;
    int chosenLevel = -1;
    static final String LEVEL_KEY = "level";

    Level[] levels = new Level[]{
            new Level(1000, 1000, SettingsActivity.images.get(SettingsActivity.GEOMETRIC_FIGURES))
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_levels);

        main = new Intent(LevelsActivity.this, MainActivity.class);
        currentUnlockedLevel = MainMenuActivity.preferences.getInt(MainMenuActivity.CURRENT_UNLOCKED_LEVEL_KEY, 0);

        for (int i = 0; i < levelButtons.length; i++) levelButtons[i] = findViewById(levelButtonIds[i]);

        for (int i = 0; i < levelButtons.length; i++) {
            final int i1 = i;
            levelButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i1 <= currentUnlockedLevel) {
                        if (chosenLevel != i1) {
                            chosenLevel = i1;
                            markButton();
                        }
                        else {
                            levelButtons[i1].setImageResource(R.mipmap.it_cube_logo_background);
                            chosenLevel = -1;
                        }
                    }
                }
            });
        }
        showBlockedLevels();
    }

    public void markButton() {
        for (int i = 0; i < levelButtons.length; i++) {
            if (i == chosenLevel) {
                levelButtons[i].setImageResource(R.drawable.ic_baseline_crop_din_100);
            } else levelButtons[i].setImageResource(R.mipmap.it_cube_logo_background);
        }
        showBlockedLevels();
    }

    public void showBlockedLevels() {
        for (int i = levelButtons.length - 1; i > currentUnlockedLevel; i--) {
            levelButtons[i].setImageResource(R.drawable.ic_baseline_close_24);
            ((TextView) findViewById(levelNumberIds[i])).setTextColor(getResources().getColor(R.color.light_gray));
        }
    }

    public void chooseLevel(View view) {
        if (chosenLevel == -1) return;
        main.putExtra(LEVEL_KEY, levels[chosenLevel]);
        startActivity(main);
        //levelButtons[chosenButton].setImageResource(R.mipmap.it_cube_logo_background);
    }

    public void exitToMainMenu(View view) {
        finish();
    }
}