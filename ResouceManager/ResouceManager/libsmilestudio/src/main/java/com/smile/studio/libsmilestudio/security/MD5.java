package com.smile.studio.libsmilestudio.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String encrypt(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte[] byteData = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public static String customMD5(String str) {
        String str2 = "MD5";
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest) {
                str2 = Integer.toHexString(b & 255);
                while (str2.length() < 2) {
                    str2 = "0" + str2;
                }
                stringBuilder.append(str2);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String encrypt(String string, int nLoop) {
        if (nLoop <= 1)
            return encrypt(string);
        return encrypt(encrypt(string), nLoop -= 1);
    }
}
