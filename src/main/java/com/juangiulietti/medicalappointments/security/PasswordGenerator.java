package com.juangiulietti.medicalappointments.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {
    private static final String ALPHABET =
            "ABCDEFGHJKLMNPQRSTUVWXYZ" +
                    "abcdefghijkmnopqrstuvwxyz" +
                    "23456789";

    private static final int PASSWORD_LENGTH = 12;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generate(){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < PASSWORD_LENGTH; i++){
            int index = secureRandom.nextInt(ALPHABET.length());
            char character = ALPHABET.charAt(index);
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }
}
