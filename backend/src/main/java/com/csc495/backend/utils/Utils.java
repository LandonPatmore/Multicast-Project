package com.csc495.backend.utils;

import java.util.Random;

public class Utils {

    public static int[] generateColor() {
        final int[] color = new int[3];
        final Random r = new Random();

        color[0] = r.nextInt(256);
        color[1] = r.nextInt(256);
        color[2] = r.nextInt(256);

        return color;
    }
}
