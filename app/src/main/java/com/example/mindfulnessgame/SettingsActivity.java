package com.example.mindfulnessgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.TreeMap;

public class SettingsActivity extends AppCompatActivity {

    static TreeMap<String, int[]> images = new TreeMap<>();

    static final String GEOMETRIC_FIGURES = "Геометричесике фигуры";
    static final String LOGOS = "Логотипы";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
    }
}