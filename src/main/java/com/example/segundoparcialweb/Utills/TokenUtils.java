package com.example.segundoparcialweb.Utills;


import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class TokenUtils {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateToken(int bytes) {
        byte[] b = new byte[bytes];
        secureRandom.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    public static String generateCodigo(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = secureRandom.nextInt(ALPHANUM.length());
            sb.append(ALPHANUM.charAt(idx));
        }
        return sb.toString();
    }
}
