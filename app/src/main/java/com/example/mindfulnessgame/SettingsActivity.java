package com.example.mindfulnessgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    static final String BACKGROUND_COLOR = "backgroundColor";
    static final String TEXT_BACKGROUND_COLOR = "textBackgroundColor";

    ListView bgColors, textBgColors;
    ImageView chosenColor;

    static ArrayList<int[]> images = new ArrayList<>();
    Color[] colors = {
            new Color("#f01ff0"), new Color("#88008c"), // розовые цвета
            //new Color("#fbff00"), new Color("#c4c702"), // жёлтые; с тёмной темой они превращаются в оттенки г-на, поэтому я их отключил
            new Color("#2668ff"), new Color("#2403fc"), // синие
            new Color("#18d902"), new Color("#11ad00"), // зелёные
            new Color("#e30b00"), new Color("#ad0900"), // красные
            new Color("#aa00e3"), new Color("#7c00c9"), // фиолетовые
            new Color("#00e3c5"), new Color("#00baa1"), // бирюзовые
            new Color("#5c5c5c"), new Color("#2b2b2b")  // серые
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        Color[] backgroundColors = new Color[colors.length];
        for (int i = 0; i < backgroundColors.length; i++) backgroundColors[i] = new Color(colors[i].resource);
        String bgColor = MainMenuActivity.preferences.getString(BACKGROUND_COLOR, "#f01ff0");
        for (Color backgroundColor: backgroundColors) {
            if (backgroundColor.resource.equals(bgColor)) {
                backgroundColor.isSelected = true;
                break;
            }
        }
        chosenColor = findViewById(R.id.chosen_color_bg_IV);
        chosenColor.setBackgroundColor(android.graphics.Color.parseColor(bgColor));
        bgColors = findViewById(R.id.bg_colors_LV);
        bgColors.setAdapter(new Color("").new Adapter(this, backgroundColors, chosenColor));


        Color[] textBackgroundColors = new Color[colors.length];
        for (int i = 0; i < textBackgroundColors.length; i++) textBackgroundColors[i] = new Color(colors[i].resource);
        String textBgColor = MainMenuActivity.preferences.getString(TEXT_BACKGROUND_COLOR, "#88008c");
        for (Color textBackgroundColor: textBackgroundColors) {
            if (textBackgroundColor.resource.equals(textBgColor)) {
                textBackgroundColor.isSelected = true;
                break;
            }
        }
        chosenColor = findViewById(R.id.chosen_color_text_bg_IV);
        chosenColor.setBackgroundColor(android.graphics.Color.parseColor(textBgColor));
        textBgColors = findViewById(R.id.text_bg_colors_LV);
        textBgColors.setAdapter(new Color("").new Adapter(this, textBackgroundColors, chosenColor));
    }

    public static void fillImages() {
        images.add(new int[]{
                R.drawable.yellow_square, R.drawable.orange_square, R.drawable.red_square,
                R.drawable.blue_circle, R.drawable.blue_capsule, R.drawable.blue_elipse,
                R.drawable.green_triangle_0, R.drawable.green_triangle_90, R.drawable.green_triangle_180,
                R.drawable.purple_rhombus_0, R.drawable.purple_rhombus_90, R.drawable.cyan_hexagon_0,
                R.drawable.cyan_hexagon_90, R.drawable.yellow_star, R.drawable.orange_star
        });
        images.add(new int[]{
                R.drawable.it_cube_logo, R.drawable.samsung_logo, R.drawable.it_school_logo,
                R.drawable.windows_logo, R.drawable.java_logo, R.drawable.android_logo,
                R.drawable.jetbrains_logo, R.drawable.discord_logo
        });
        images.add(new int[]{
                R.drawable.alarm_clock, R.drawable.apple, R.drawable.bottle,
                R.drawable.car, R.drawable.cup, R.drawable.pencil,
                R.drawable.smartphone, R.drawable.table_lamp, R.drawable.tree
        });
    }

    public void exitToMainMenu(View view) {
        MainMenuActivity.playClickSound(this);

        finish();
    }

    public void saveChanges(View view) {
        MainMenuActivity.playClickSound(this);

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

    class Color {
        String resource;

        boolean isSelected;

        public Color(String  resource) {
            this.resource = resource;
        }

        class Adapter extends ArrayAdapter<Color> {

            ImageView chosenColor;

            Color[] colors;

            public Adapter(Context context, Color[] colors, ImageView chosenColor) {
                super(context, R.layout.color_item, colors);
                this.colors = colors;
                this.chosenColor = chosenColor;
            }

            @SuppressLint("InflateParams")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                colors[position] = getItem(position);
                if (convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.color_item, null);

                ImageButton select = convertView.findViewById(R.id.color_IB);
                select.setBackgroundColor(android.graphics.Color.parseColor(colors[position].resource));
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainMenuActivity.playClickSound(SettingsActivity.this);

                        for (Color color: colors) color.isSelected = false;
                        colors[position].isSelected = true;

                        chosenColor.setBackgroundColor(android.graphics.Color.parseColor(colors[position].resource));
                    }
                });
                if (colors[position].isSelected) chosenColor.setBackgroundColor(android.graphics.Color.parseColor(colors[position].resource));
                return convertView;
            }
        }
    }
}