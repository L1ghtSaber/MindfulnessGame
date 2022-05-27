package com.example.mindfulnessgame;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    static final String HIGH_SCORE = "highScore";

    TextView timerView, attemptsLeft, levelNumber;
    ImageView image, answerImage;
    EditText answerAmount;
    ImageButton hideBtn;

    TreeMap<Integer, Integer> rightAnswers = new TreeMap<>();
    ArrayList<Answer> playerAnswers = new ArrayList<>();
    Level level;

    int wrongAnswersCount, highScore, currentAnswer;
    boolean endlessMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        timerView = findViewById(R.id.timer_TV);
        timerView.setBackgroundColor(Color.argb(100, 0, 0, 0));
        timerView.setClickable(true);
        attemptsLeft = findViewById(R.id.attempts_left_TV);
        levelNumber = findViewById(R.id.level_number_TV);
        image = findViewById(R.id.image_IV);
        answerImage = findViewById(R.id.answer_image_IV);
        answerAmount = findViewById(R.id.answer_amount_ET);
        hideBtn = findViewById(R.id.hide_IB);
        hideBtn.setBackgroundColor(Color.parseColor(MainMenuActivity.preferences.getString(SettingsActivity.BACKGROUND_COLOR, "#f01ff0")));

        level = (Level) getIntent().getSerializableExtra(LevelsActivity.LEVEL);
        String out = "УРОВЕНЬ: " + (level.number + 1);
        levelNumber.setText(out);

        endlessMode = getIntent().getBooleanExtra(MainMenuActivity.ENDLESS_MODE, false);
        highScore = MainMenuActivity.preferences.getInt(HIGH_SCORE, 0);

        class Timer extends View {

            final CountDownTimer timer;

            public Timer(Context context) {
                super(context);
                final int[] count = {3};
                timer = new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String out = "\n\n\n" + ((count[0] <= 0) ? "ВПЕРЁД!" : count[0]);
                        timerView.setText(out);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (endlessMode) {
            Toast.makeText(this, "ВЫ ПРОШЛИ НА " + (level.number - highScore) + " УРОВНЕЙ БОЛЬШЕ,\nЧЕМ В ЛУЧШИЙ РАЗ", Toast.LENGTH_SHORT).show();

            if (level.number > highScore) {
                MainMenuActivity.editor.putInt(HIGH_SCORE, level.number);
                MainMenuActivity.editor.commit();
            }
        }
    }

    public void start() {
        class ImageTimer extends View {

            final CountDownTimer timer;

            public ImageTimer(Context context) {
                super(context);

                hideBtn.setClickable(true);
                hideBtn.setBackgroundColor(Color.parseColor(MainMenuActivity.preferences.getString(SettingsActivity.BACKGROUND_COLOR, "#f01ff0")));
                answerAmount.setCursorVisible(false);
                String out = "УРОВЕНЬ: " + (level.number + 1);
                levelNumber.setText(out);
                rightAnswers.clear();
                playerAnswers.clear();
                currentAnswer = 0;

                timer = new CountDownTimer(1000 * 60 * 60, 1) {
                    @Override
                    public void onTick(long millisUntilFinished) {
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
                        level.timeCount++;
                    }

                    @Override
                    public void onFinish() {
                        hideBtn.setBackgroundColor(0);
                        hideBtn.setClickable(false);
                        answerAmount.setCursorVisible(true);
                        image.setImageResource(0);

                        for (int i = 0; i < level.images.length; i++) {
                            if (rightAnswers.containsKey(level.images[i])) {
                                int oldAmount = rightAnswers.get(level.images[i]);
                                rightAnswers.put(level.images[i], oldAmount + 1);
                            }
                            else rightAnswers.put(level.images[i], 1);
                        }
                        rightAnswers.put(level.getExtraImage(), 0);

                        for (Map.Entry<Integer, Integer> entry: rightAnswers.entrySet())
                            playerAnswers.add(new Answer(entry.getKey(), -1));
                        Collections.sort(playerAnswers, new Comparator<Answer>() {
                            @Override
                            public int compare(Answer o1, Answer o2) {
                                int r = (int) (Math.random() * 3);
                                if (r == 0) return -1;
                                else if (r == 1) return 0;
                                else return 1;
                            }
                        });

                        playerAnswers.get(currentAnswer).amount = 0;
                        answerImage.setImageResource(playerAnswers.get(currentAnswer).image);
                        answerAmount.setText("0");

                        cancel();
                    }
                };
            }
        }
        new ImageTimer(this).timer.start();
    }

    public void changeLevelImage(View view) {
        MainMenuActivity.playClickSound(this);

        if (answerAmount.getText().toString().equals("10203")) {
            answerAmount.setText("");
            playerAnswers.clear();
            for (Map.Entry<Integer, Integer> entry: rightAnswers.entrySet())
                playerAnswers.add(new Answer(entry.getKey(), entry.getValue()));

            return;
        }
        try {
            playerAnswers.get(currentAnswer).amount = Integer.parseInt(answerAmount.getText().toString());
        } catch (IllegalStateException | NumberFormatException e) {
            answerAmount.setText("0");
        }

        if (view.getId() == R.id.previous_image_IB) currentAnswer--;
        else if (view.getId() == R.id.next_image_IB) currentAnswer++;

        if (currentAnswer < 0) currentAnswer = playerAnswers.size() - 1;
        else if (currentAnswer >= playerAnswers.size()) currentAnswer = 0;

        if (playerAnswers.get(currentAnswer).amount < 0) playerAnswers.get(currentAnswer).amount = 0;

        answerImage.setImageResource(playerAnswers.get(currentAnswer).image);
        String out = "" + playerAnswers.get(currentAnswer).amount;
        answerAmount.setText(out);
    }

    public void checkAnswers(View view) {
        MainMenuActivity.playClickSound(this);

        if (playerAnswers.isEmpty()) return;
        try {
            playerAnswers.get(currentAnswer).amount = Integer.parseInt(answerAmount.getText().toString());
        } catch (IllegalStateException | NumberFormatException e) {
            Toast.makeText(this, "УПС\nВЫ ЗАПОЛНИЛИ НЕ ВСЕ ПОЛЯ ОТВЕТОВ", Toast.LENGTH_SHORT).show();
            return;
        }

        TreeMap<Integer, Integer> playerAnswers = new TreeMap<>();
        for (int i = 0; i < this.playerAnswers.size(); i++)
            playerAnswers.put(this.playerAnswers.get(i).image, this.playerAnswers.get(i).amount);

        if (playerAnswers.containsValue(-1)) {
            Toast.makeText(this, "УПС\nВЫ ЗАПОЛНИЛИ НЕ ВСЕ ПОЛЯ ОТВЕТОВ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (playerAnswers.entrySet().equals(rightAnswers.entrySet())) {
            if (!endlessMode) {
                Toast.makeText(this, "ВЫ УСПЕШНО ПРОШЛИ ЭТОТ УРОВЕНЬ =)", Toast.LENGTH_LONG).show();
                if (level.number + 2 > MainMenuActivity.preferences.getInt(LevelsActivity.CURRENT_UNLOCKED_LEVEL, 0)) {
                    MainMenuActivity.editor.putInt(LevelsActivity.CURRENT_UNLOCKED_LEVEL, level.number + 2);
                    MainMenuActivity.editor.commit();
                }
                finish();
            }
            else {
                level.imageTime -= 10;
                if (level.imageTime <= 0) level.imageTime = 1;

                if (level.number % 2 == 0) level.switchTime -= 20;
                if (level.switchTime <= 0) level.switchTime = 1;

                int amount = level.images.length;
                if (level.number % 3 == 0) amount++;

                level.number++;

                if (level.number % 4 == 0) level.allowedImages.add(SettingsActivity.images.get((int) (Math.random() * SettingsActivity.images.size())));

                level = new Level(level.imageTime, level.switchTime, level.allowedImages, amount, level.number);

                start();
            }
        } else {
            wrongAnswersCount++;

            attemptsLeft.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_shape));
            String out = "ПОПЫТОК ОСТАЛОСЬ: " + (((endlessMode) ? 5 : 3) - wrongAnswersCount);
            attemptsLeft.setText(out);

            if (wrongAnswersCount >= ((endlessMode) ? 5 : 3)) {
                if (!endlessMode)
                    Toast.makeText(this, "ВАМ НЕ УДАЛОСЬ ПРОЙТИ ЭТОТ УРОВЕНЬ =(\nПОПРОБУЙТЕ СНОВА", Toast.LENGTH_LONG).show();
                finish();
            } else
                Toast.makeText(this, "ВАШ ОТВЕТ НЕПРАВИЛЬНЫЙ\nПОПРОБУЙТЕ СНОВА", Toast.LENGTH_LONG).show();
        }
    }

    public void exitToMainMenu(View view) {
        MainMenuActivity.playClickSound(this);

        finish();
    }

    public void changeAmount(View view) {
        MainMenuActivity.playClickSound(this);

        if (view.getId() == R.id.amount_minus_IB) playerAnswers.get(currentAnswer).amount--;
        else if (view.getId() == R.id.amount_plus_IB) playerAnswers.get(currentAnswer).amount++;

        if (playerAnswers.get(currentAnswer).amount < 0) playerAnswers.get(currentAnswer).amount = 0;

        String out = "" + playerAnswers.get(currentAnswer).amount;
        answerAmount.setText(out);
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