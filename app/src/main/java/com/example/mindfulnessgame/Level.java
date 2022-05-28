package com.example.mindfulnessgame;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {

    ArrayList<int[]> allowedImages = new ArrayList<>(); // изображения, из которых выбираются изображения для уровня
    int[] images;

    int imageTime, // время отображения изображения в поле
            switchTime, // время смены изображения
            timeCount, // счётчик для времени
            number, // порядковый номер уровня
            currentImageNumber; // номер текущего изображения в поле
    boolean imageOn; // если изображение находится в поле

    public Level(int imageTime, int switchTime, ArrayList<int[]> allowedImages, int imagesAmount, int number) {
        this.imageTime = imageTime;
        this.switchTime = switchTime;
        this.allowedImages.addAll(allowedImages);
        images = new int[imagesAmount]; // изображения уровня случайным образом заполняются
        for (int i = 0; i < images.length; i++) {
            int rand1 = (int) (Math.random() * this.allowedImages.size()),
                    rand2 = (int) (Math.random() * this.allowedImages.get(rand1).length);
            images[i] = this.allowedImages.get(rand1)[rand2];
        }
        this.number = number;
    }

    public int getExtraImage() { // метод для получения лишнего изображения (того, которого нет среди изображений уровня)
        int rand = (int) (Math.random() * allowedImages.size());
        boolean isExtra = false;
        int image = 0;
        while (!isExtra) {
            image = allowedImages.get(rand)[(int) (Math.random() * allowedImages.get(rand).length)];
            for (int img : images) {
                isExtra = image != img;
                if (!isExtra) break;
            }
        }
        return image;
    }
}
