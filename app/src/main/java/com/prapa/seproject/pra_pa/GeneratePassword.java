package com.prapa.seproject.pra_pa;

import java.security.SecureRandom;

public class GeneratePassword {

    private static SecureRandom secureRandom = new SecureRandom();

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";

    public static String generatePassword() {
        int len = 10;
        String dic = ALPHA_CAPS+ALPHA+NUMERIC;
        String result = "";
        for (int i = 0; i < len; i++) {
            int index = secureRandom.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

}
