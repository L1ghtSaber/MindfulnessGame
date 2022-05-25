package com.example.mindfulnessgame;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {

    int imageTime, switchTime, timeCount;
    boolean imageOn;
    int currentImageNumber;
    int[] images;
    int number;

    public Level(int imageTime, int switchTime, ArrayList<int[]> images, int imagesAmount, int number) {
        this.imageTime = imageTime;
        this.switchTime = switchTime;
        this.images = new int[imagesAmount];
        for (int i = 0; i < this.images.length; i++) {
            int rand = (int) (Math.random() * images.size());
            this.images[i] = images.get(rand)[(int) (Math.random() * images.get(rand).length)];
        }
        this.number = number;
    }
}
