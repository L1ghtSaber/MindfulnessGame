package com.example.mindfulnessgame;

import java.io.Serializable;

public class Level implements Serializable {

    int imageTime, switchTime, timeCount;
    boolean imageOn;
    int currentImageNumber;
    int[] images;
    int number;

    public Level(int imageTime, int switchTime, int[][] images, int imagesAmount, int number) {
        this.imageTime = imageTime;
        this.switchTime = switchTime;
        this.images = new int[imagesAmount];
        for (int i = 0; i < this.images.length; i++) {
            int rand = (int) (Math.random() * images.length);
            this.images[i] =
                    images[rand][(int) (Math.random() * images[rand].length)];
        }
        this.number = number;
    }
}
