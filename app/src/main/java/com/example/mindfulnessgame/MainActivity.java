package com.example.mindfulnessgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    TextView timerView;
    ImageView image, answerImage;
    EditText answerAmount;
    ImageButton hideBtn;

    TreeMap<Integer, Integer> rightAnswers = new TreeMap<>();
    ArrayList<Answer> playerAnswers = new ArrayList<>();
    Level level;

    int wrongAnswersCount;
    int currentAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        timerView = findViewById(R.id.timer_TV);
        timerView.setBackgroundColor(Color.argb(100, 0, 0, 0));
        timerView.setClickable(true);
        image = findViewById(R.id.image_IV);
        answerImage = findViewById(R.id.answer_image_IV);
        answerAmount = findViewById(R.id.answer_amount_ET);
        hideBtn = findViewById(R.id.hide_IB);
        hideBtn.setBackgroundColor(Color.parseColor(MainMenuActivity.preferences.getString(SettingsActivity.BACKGROUND_COLOR, "#f01ff0")));
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        level = (Level) getIntent().getSerializableExtra(LevelsActivity.LEVEL_KEY);

        class Timer extends View {

            CountDownTimer timer;

            public Timer(Context context) {
                super(context);
                final int[] count = {3};
                timer = new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (count[0] <= 0) timerView.setText("\n\n\nВПЕРЁД!");
                        else timerView.setText("\n\n\n" + count[0]);
                        count[0]--;
                    }

                    @Override
                    public void onFinish() {
                        timerView.setText("");
                        timerView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                        timerView.setClickable(false);

                        MainActivity.this.start();
                        cancel();
                    }
                };
            }
        }
        new Timer(this).timer.start();
    }

    public void start() {
        class ImageTimer extends View {

            CountDownTimer timer;

            public ImageTimer(Context context) {
                super(context);
                timer = new CountDownTimer(1000 * 60, 1) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        level.timeCount++;

                        if (level.imageOn) {
                            if (level.timeCount % level.imageTime == 0) {
                                level.imageOn = false;
                                image.setImageResource(0);
                                level.timeCount = 0;
                            }
                        } else {
                            if (level.timeCount % level.switchTime == 0) {
                                if (level.currentImageNumber >= level.images.length) {
                                    onFinish();
                                    return;
                                }
                                level.imageOn = true;
                                image.setImageResource(level.images[level.currentImageNumber]);
                                level.currentImageNumber++;
                                level.timeCount = 0;
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        hideBtn.setBackgroundColor(0);
                        hideBtn.setClickable(false);

                        image.setImageResource(0);

                        for (int i = 0; i < level.images.length; i++) {
                            if (rightAnswers.containsKey(level.images[i])) {
                                int oldAmount = rightAnswers.get(level.images[i]);
                                rightAnswers.put(level.images[i], oldAmount + 1);
                            }
                            else rightAnswers.put(level.images[i], 1);
                        }

                        for (Map.Entry<Integer, Integer> entry: rightAnswers.entrySet())
                            playerAnswers.add(new Answer(entry.getKey(), entry.getValue()));
                        Collections.sort(playerAnswers, new Comparator<Answer>() {
                            @Override
                            public int compare(Answer o1, Answer o2) {
                                int r = (int) (Math.random() * 3);
                                if (r == 0) return -1;
                                else if (r == 1) return 0;
                                else return 1;
                            }
                        });

                        answerImage.setImageResource(playerAnswers.get(currentAnswer).image);
                        String out = "" + ((playerAnswers.get(currentAnswer).amount == 0) ? "" : playerAnswers.get(currentAnswer).amount);
                        answerAmount.setText(out);
                        cancel();
                    }
                };
            }
        }
        new ImageTimer(this).timer.start();
    }

    public void changeLevelImage(View view) {
        try {
            playerAnswers.get(currentAnswer).amount = Integer.parseInt(answerAmount.getText().toString());
        } catch (IllegalStateException | NumberFormatException e) {
            answerAmount.setText("");
        }

        if (view.getId() == R.id.previous_image_IB) currentAnswer--;
        else if (view.getId() == R.id.next_image_IB) currentAnswer++;

        if (currentAnswer < 0) currentAnswer = playerAnswers.size() - 1;
        else if (currentAnswer >= playerAnswers.size()) currentAnswer = 0;

        answerImage.setImageResource(playerAnswers.get(currentAnswer).image);
        String out = "" + ((playerAnswers.get(currentAnswer).amount == 0) ? "" : playerAnswers.get(currentAnswer).amount);
        answerAmount.setText(out);
    }

    public void checkAnswers(View view) {
        if (playerAnswers.isEmpty()) return;

        TreeMap<Integer, Integer> playerAnswers = new TreeMap<>();
        for (int i = 0; i < this.playerAnswers.size(); i++)
            playerAnswers.put(this.playerAnswers.get(i).image, this.playerAnswers.get(i).amount);

        if (playerAnswers.entrySet().equals(rightAnswers.entrySet())) {
            Toast.makeText(this, "ВЫ УСПЕШНО ПРОШЛИ ЭТОТ УРОВЕНЬ =)", Toast.LENGTH_LONG).show();
            if (level.number + 1 > MainMenuActivity.preferences.getInt(MainMenuActivity.CURRENT_UNLOCKED_LEVEL_KEY, 0)) {
                MainMenuActivity.editor.putInt(MainMenuActivity.CURRENT_UNLOCKED_LEVEL_KEY, level.number + 1);
                MainMenuActivity.editor.commit();
            }
            finish();
        } else {
            wrongAnswersCount++;
            if (wrongAnswersCount >= 3) {
                Toast.makeText(this, "ВАМ НЕ УДАЛОСЬ ПРОЙТИ ЭТОТ УРОВЕНЬ =(\nПОПРОБУЙТЕ СНОВА", Toast.LENGTH_LONG).show();
                finish();
            } else
                Toast.makeText(this, "ВАШ ОТВЕТ НЕПРАВИЛЬНЫЙ\nПОПРОБУЙТЕ СНОВА", Toast.LENGTH_LONG).show();
        }
    }

    public void exitToMainMenu(View view) {
        finish();
    }

    static class Answer {
        int image;
        int amount;

        public Answer(int image, int amount) {
            this.image = image;
            this.amount = amount;
        }
    }
}