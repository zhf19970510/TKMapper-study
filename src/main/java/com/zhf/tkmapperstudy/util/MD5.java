package com.zhf.tkmapperstudy.util;

import java.security.MessageDigest;

public class MD5 {
    /**
     * 使用:
     * 直接使用即可,至于具体参数,看下面方法的说明即可;
     * MD5Encode();  MD5不可逆简单加密:散列函数-基于HASH算法的编码
     */

    // 16进制的字符数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String MD5Encode(String source) {
        return MD5Encode(source, "UTF8", true);
    }

    /**
     * @param source    原字符串
     * @param encoding  指定编码类型
     * @param uppercase 是否转为大写字符串
     */
    public static String MD5Encode(String source, String encoding, boolean uppercase) {
        String result = null;
        try {
            result = source;
            // 获得MD5摘要对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组更新摘要信息
            messageDigest.update(result.getBytes(encoding));
            result = byteArrayToHexString(messageDigest.digest());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uppercase ? result.toUpperCase() : result;
    }

    //byte转16进制
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte tem : bytes) {
            stringBuilder.append(byteToHexString(tem));
        }
        return stringBuilder.toString();
    }

    //16进制转byte[]
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
