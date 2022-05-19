package com.example.mindfulnessgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    TextView timerView;
    ImageView image;
    ListView answers;

    static TreeMap<Integer, Integer> rightAnswers = new TreeMap<>();

    int wrongAnswersCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        timerView = findViewById(R.id.timer_TV);
        timerView.setBackgroundColor(Color.argb(100, 0, 0, 0));
        image = findViewById(R.id.image_IV);
        answers = findViewById(R.id.answers_LV);


        class Timer extends View {

            public Timer(Context context) {
                super(context);
                final int[] count = {3};
                CountDownTimer timer = new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (count[0] <= 0) timerView.setText("\n\n\nВПЕРЁД!");
                        else timerView.setText("\n\n\n" + count[0]);
                        count[0]--;
                    }

                    @Override
                    public void onFinish() {
                        timerView.setText("");
                        timerView.setBackgroundColor(Color.argb(0,0,0,0));

                        MainActivity.this.start((Level) getIntent().getSerializableExtra(LevelsActivity.LEVEL_KEY));
                    }
                }.start();
            }
        }
        Timer timer = new Timer(this);
    }

    public void start(Level level) {
        class ImageTimer extends View {

            public ImageTimer(Context context) {
                super(context);
                level.imageOn = true;
                CountDownTimer timer = new CountDownTimer(1000, 1) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        image.setImageResource(R.mipmap.it_cube_logo_background);
                        for (int i = 0; i < level.images.length; i++) {
                            if (rightAnswers.containsKey(level.images[i])) {
                                int oldAmount = rightAnswers.get(level.images[i]);
                                rightAnswers.put(level.images[i], level.number + 1);
                            }
                            else rightAnswers.put(level.images[i], 1);
                        }

                        ArrayList<Answer> answersArrayList = new ArrayList<>();
                        for (Map.Entry<Integer, Integer> entry: rightAnswers.entrySet())
                            answersArrayList.add(new Answer(entry.getKey(), entry.getValue()));
                        int rand = (int) (Math.random() * answersArrayList.size());
                        answersArrayList.add(rand, new Answer(R.drawable.ic_baseline_close_24, 0));
                        rightAnswers.put(answersArrayList.get(rand).image, 0);

                        Answer[] answersArray = new Answer[answersArrayList.size()];
                        for (int i = 0; i < answersArray.length; i++) answersArray[i] = answersArrayList.get(i);
                        answers.setAdapter(new Answer.Adapter(MainActivity.this, answersArray));
                    }
                }.start();
            }
        }
        ImageTimer imageTimer = new ImageTimer(this);
    }

    public void checkAnswers(View view) {
        if (answers.getAdapter() == null) return;

        Answer.Adapter adapter = (Answer.Adapter) answers.getAdapter();
        TreeMap<Integer, Integer> playerAnswers = adapter.getPlayerAnswers();

        if (playerAnswers.entrySet().equals(rightAnswers.entrySet())) {
            Toast.makeText(this, "ВЫ УСПЕШНО ПРОШЛИ ЭТОТ УРОВЕНЬ =)", Toast.LENGTH_LONG).show();
            int oldUnlockedLevel = MainMenuActivity.preferences.getInt(MainMenuActivity.CURRENT_UNLOCKED_LEVEL_KEY, 0);
            MainMenuActivity.editor.putInt(MainMenuActivity.CURRENT_UNLOCKED_LEVEL_KEY, oldUnlockedLevel + 1);
            MainMenuActivity.editor.commit();
            finish();
        } else {
            wrongAnswersCount++;
            if (wrongAnswersCount >= 3) {
                Toast.makeText(this, "ВАМ НЕ УДАЛОСЬ ПРОЙТИ ЭТОТ УРОВЕНЬ =(\nПОПРОБУЙТЕ СНОВА", Toast.LENGTH_LONG).show();
                finish();
            } else Toast.makeText(this, "ВАШ ОТВЕТ НЕПРАВИЛЬНЫЙ\nПОПРОБУЙТЕ СНОВА", Toast.LENGTH_LONG).show();
        }
    }


    static class Answer {
        int image;
        int amount;

        public Answer(int image, int amount) {
            this.image = image;
            this.amount = amount;
        }

        static class Adapter extends ArrayAdapter<Answer> {

            private final View[] convertView;

            public Adapter(Context context, Answer[] answers) {
                super(context, R.layout.answer_item, answers);
                convertView = new View[answers.length];
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                final Answer answer = getItem(position);
                if (convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.answer_item, null);

                ((ImageView) convertView.findViewById(R.id.answer_image_IV)).setImageResource(answer.image);

                this.convertView[position] = convertView;
                return convertView;
            }

            public TreeMap<Integer, Integer> getPlayerAnswers() {
                TreeMap<Integer, Integer> playerAnswers = new TreeMap<>();
                for (int i = 0; i < this.getCount(); i++) {
                    int amount = Integer.parseInt(((EditText) convertView[i].findViewById(R.id.answer_amount_ET)).getText().toString());
                    playerAnswers.put(this.getItem(i).image, amount);
                }
                return playerAnswers;
            }
        }
    }
}