package com.example.mindfulnessgame;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {

    ArrayList<int[]> allowedImages;

    int imageTime, switchTime, timeCount;
    boolean imageOn;
    int currentImageNumber;
    int[] images;
    int number;

    public Level(int imageTime, int switchTime, ArrayList<int[]> allowedImages, int imagesAmount, int number) {
        this.imageTime = imageTime;
        this.switchTime = switchTime;
        this.allowedImages = allowedImages;
        images = new int[imagesAmount];
        for (int i = 0; i < images.length; i++) {
            int rand = (int) (Math.random() * allowedImages.size());
            images[i] = allowedImages.get(rand)[(int) (Math.random() * allowedImages.get(rand).length)];
        }
        this.number = number;
    }
}
