package com.example.mindfulnessgame;

import java.io.Serializable;
import java.util.ArrayList;


public class Level implements Serializable {

    ArrayList<int[]> allowedImages = new ArrayList<>();
    int[] images;

    int imageTime, switchTime, timeCount;
    boolean imageOn;
    int currentImageNumber;
    int number;

    public Level(int imageTime, int switchTime, ArrayList<int[]> allowedImages, int imagesAmount, int number) {
        this.imageTime = imageTime;
        this.switchTime = switchTime;
        this.allowedImages.addAll(allowedImages);
        images = new int[imagesAmount];
        for (int i = 0; i < images.length; i++) {
            int rand1 = (int) (Math.random() * this.allowedImages.size()),
                    rand2 = (int) (Math.random() * this.allowedImages.get(rand1).length);
            images[i] = allowedImages.get(rand1)[rand2];
        }
        this.number = number;
    }

    public int getExtraImage() {
        int rand = (int) (Math.random() * allowedImages.size());
        boolean isExtra = false;
        int image = 0;
        while (!isExtra) {
            image = allowedImages.get(rand)[(int) (Math.random() * allowedImages.get(rand).length)];
            for (int i = 0; i < images.length; i++) {
                isExtra = image != images[i];
                if (!isExtra) break;
            }
        }
        return image;
    }
}
