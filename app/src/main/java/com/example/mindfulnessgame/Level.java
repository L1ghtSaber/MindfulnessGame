package com.example.mindfulnessgame;

import java.io.Serializable;

public class Level implements Serializable {

    int imageTime;
    int imageTimeCount;
    int switchTime;
    int switchTimeCount;
    boolean imageOn;
    int imageCount;
    int[] images;
    int number;

    public Level(int imageTime, int switchTime, int[] images, int number) {
        this.images = new int[images.length];
        for (int i = 0; i < images.length; i++) this.images[i] = images[(int) (Math.random() * images.length)];
        this.switchTime = switchTime;
        this.images = images;
        this.number = number;

        imageTimeCount = this.imageTime;
    }
}
