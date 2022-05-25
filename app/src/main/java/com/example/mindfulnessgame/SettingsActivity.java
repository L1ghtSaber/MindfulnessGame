package com.example.mindfulnessgame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class SettingsActivity extends AppCompatActivity {

    static final String BACKGROUND_COLOR = "backgroundColor";
    static final String TEXT_BACKGROUND_COLOR = "textBackgroundColor";
    static final String ALLOWED_IMAGES = "allowedImages";
    static final String GEOMETRIC_FIGURES = "Геометричесике фигуры";
    static final String LOGOS = "Логотипы";

    ListView bgColors, textBgColors;

    Color[] colors = {
            new Color("#88008c"), new Color("#f01ff0") // розовые цвета
    };

    static TreeMap<String, int[]> images = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        Color[] backgroundColors = new Color[colors.length];
        for (int i = 0; i < backgroundColors.length; i++) backgroundColors[i] = new Color(colors[i].resource);
        String bgColor = MainMenuActivity.preferences.getString(BACKGROUND_COLOR, "#f01ff0");
        for (int i = 0; i < backgroundColors.length; i++) {
            if (backgroundColors[i].resource.equals(bgColor)) {
                backgroundColors[i].isSelected = true;
                Color color = backgroundColors[0];
                backgroundColors[0] = backgroundColors[i];
                backgroundColors[i] = color;
            }
        }
        bgColors = findViewById(R.id.bg_colors_LV);
        bgColors.setAdapter(new Color.Adapter(this, backgroundColors));


        Color[] textBackgroundColors = new Color[colors.length];
        for (int i = 0; i < textBackgroundColors.length; i++) textBackgroundColors[i] = new Color(colors[i].resource);
        String textBgColor = MainMenuActivity.preferences.getString(TEXT_BACKGROUND_COLOR, "#88008c");
        for (int i = 0; i < textBackgroundColors.length; i++) {
            if (textBackgroundColors[i].resource.equals(textBgColor)) {
                textBackgroundColors[i].isSelected = true;
                Color color = textBackgroundColors[0];
                textBackgroundColors[0] = textBackgroundColors[i];
                textBackgroundColors[i] = color;
            }
        }
        textBgColors = findViewById(R.id.text_bg_colors_LV);
        textBgColors.setAdapter(new Color.Adapter(this, textBackgroundColors));
    }

    public static void fillImages() {
        images.put(GEOMETRIC_FIGURES, new int[]{
                R.mipmap.yellow_square_foreground, R.mipmap.orange_square_foreground, R.mipmap.red_square_foreground,
                R.mipmap.blue_circle_foreground, R.mipmap.blue_capsule_foreground, R.mipmap.blue_elipse_foreground,
                R.mipmap.green_triangle_0_foreground, R.mipmap.green_triangle_90_foreground, R.mipmap.green_triangle_180_foreground,
                R.mipmap.pueple_rhombus_0_foreground, R.mipmap.pueple_rhombus_90_foreground, R.mipmap.cyan_hexagon_0_foreground,
                R.mipmap.cyan_hexagon_90_foreground, R.mipmap.yellow_star_foreground, R.mipmap.orange_star_foreground
        });
        images.put(LOGOS, new int[]{
                R.mipmap.it_cube_logo_foreground, R.mipmap.samsung_logo_foreground, R.mipmap.it_school_logo_foreground,
                R.mipmap.windows_logo_foreground, R.mipmap.java_logo_foreground, R.mipmap.android_logo_foreground,
                R.mipmap.jetbrains_logo_foreground
        });

        String allowedImages = "";
        for (Map.Entry<String, int[]> entry: images.entrySet()) allowedImages += entry.getKey() + "|";
        MainMenuActivity.editor.putString(ALLOWED_IMAGES, allowedImages);
        MainMenuActivity.editor.commit();
    }

    public void exitToMainMenu(View view) {
        finish();
    }

    public void saveChanges(View view) {
        Color.Adapter adapter = (Color.Adapter) bgColors.getAdapter();
        String chosenColor = "";
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).isSelected) {
                chosenColor = adapter.getItem(i).resource;
                break;
            }
        }
        MainMenuActivity.editor.putString(BACKGROUND_COLOR, chosenColor);
        adapter = (Color.Adapter) textBgColors.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).isSelected) {
                chosenColor = adapter.getItem(i).resource;
                break;
            }
        }
        MainMenuActivity.editor.putString(TEXT_BACKGROUND_COLOR, chosenColor);

        MainMenuActivity.editor.commit();
        finish();
    }

    static class Color {
        ImageButton button;

        String resource;

        boolean isSelected;

        public Color(String  resource) {
            this.resource = resource;
        }

        static class Adapter extends ArrayAdapter<Color> {

            Color[] colors;

            public Adapter(Context context, Color[] colors) {
                super(context, R.layout.color_item, colors);
                this.colors = colors;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                colors[position] = getItem(position);
                if (convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.color_item, null);

                colors[position].button = convertView.findViewById(R.id.color_IB);
                colors[position].button.setBackgroundColor(android.graphics.Color.parseColor(colors[position].resource));
                colors[position].button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < colors.length; i++) colors[i].isSelected = false;
                        colors[position].isSelected = true;

                        for (int i = 0; i < colors.length; i++) colors[i].button.setImageResource(0);
                        colors[position].button.setImageResource(R.drawable.check_mark);
                    }
                });
                if (colors[position].isSelected) colors[position].button.setImageResource(R.drawable.check_mark);
                return convertView;
            }
        }
    }
}