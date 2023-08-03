package com.crking7.datn.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Utils {
    public String generateRandomCharacters(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String allLetters = upperCaseLetters + lowerCaseLetters;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allLetters.length());
            char randomChar = allLetters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
