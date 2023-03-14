package com.example.otp;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class sdkService {
    public static boolean hashCheck(Map<String, String> authParam, String hash) {
        String salt = "8i8h2hlwa0l8bd2ko1l70mcygubqa5op";
        String authString = new Gson().toJson(authParam);
        String concatenatedString = authString + salt;
        String calculatedHash = sha256(concatenatedString);
//        return authString;
//        return calculatedHash;
        return calculatedHash.equals(hash);
    }


    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}