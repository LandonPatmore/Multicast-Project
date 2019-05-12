package com.csc445.backend.utils;

import com.csc445.shared.utils.Constants;

import java.util.Random;

public class Utils {

    /**
     * THIS IS NOT A SECURE WAY TO GENERATE A PASSWORD, THIS IS JUST USED FOR DEMONSTRATION PURPOSES
     * @return
     */
    public static String passwordGenerator() {
        final Random random = new Random();
        final StringBuilder p = new StringBuilder();

        for(int i = 0; i < Constants.AES_SIZE; i++) {
            p.append(random.nextInt(10));
        }

        return p.toString();
    }
}
