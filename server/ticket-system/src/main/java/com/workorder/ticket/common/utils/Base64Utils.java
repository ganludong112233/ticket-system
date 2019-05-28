package com.workorder.ticket.common.utils;

import java.io.UnsupportedEncodingException;

import sun.misc.*;

public class Base64Utils {
    // 加密
    @SuppressWarnings("restriction")
	public static String encryptBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    // 解密
    @SuppressWarnings("restriction")
	public static String decryptBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args){
        String hello = "Hello World!";
        String enHello = encryptBase64(hello);
        String deHello = decryptBase64("eyIyMDE3MDEyMCI6eyIwdG90YWwiOiIzMDAiLCIwLTEiOiIxIiwiMS0yIjoiMiIsIjItMyI6IjMi\nLCIzLTQiOiI0IiwiNC01IjoiNSIsIjUtNiI6IjYiLCI2LTciOiI3IiwiNy04IjoiOCIsIjgtOSI6\nIjkiLCI5LTEwIjoiMTAiLCIxMC0xMSI6IjExIiwiMTEtMTIiOiIxMiIsIjEyLTEzIjoiMTMiLCIx\nMy0xNCI6IjE0IiwiMTQtMTUiOiIxNSIsIjE1LTE2IjoiMTYiLCIxNi0xNyI6IjE3IiwiMTctMTgi\nOiIxOCIsIjE4LTE5IjoiMTkiLCIxOS0yMCI6IjIwIiwiMjAtMjEiOiIyMSIsIjIxLTIyIjoiMjIi\nLCIyMi0yMyI6IjIzIiwiMjMtMCI6IjI0In19");
        System.out.println(enHello);
        System.out.println(deHello);
    }
}
