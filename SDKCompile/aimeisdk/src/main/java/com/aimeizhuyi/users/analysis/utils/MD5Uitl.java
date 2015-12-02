package com.aimeizhuyi.users.analysis.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wangyuelin on 15/7/9.
 */
public class MD5Uitl {

    public static String md5(String input) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
//            CommonUtil.printELog(LOG_TAG + "-Md5", "", e);
        }
        m.update(input.getBytes(), 0, input.length());
        byte p_md5Data[] = m.digest();

        String mOutput = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                mOutput += "0";
            // add number to string
            mOutput += Integer.toHexString(b);
        }
        // hex string to uppercase
        return mOutput.toUpperCase();
    }
}
