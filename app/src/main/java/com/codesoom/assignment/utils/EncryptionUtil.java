package com.codesoom.assignment.utils;

import java.util.Objects;

public class EncryptionUtil {
    private EncryptionUtil() {
        throw new IllegalStateException("유틸성 클래스입니다.");
    }

    /**
     * 주어진 패스워드를 암호화해서 리턴한다.
     *
     * @param password 패스워드
     * @return 암호화된 패스워드
     */
    public static String encrypt(String password) {
        return password + '♠';
    }

    /**
     * 암호화된 패스워드를 복호화한 결과를 리턴한다.
     *
     * @param encryptedPassword 암호화된 패스워드
     * @return 복호화된 패스워드
     */
    public static String decrypt(String encryptedPassword) {
        return encryptedPassword.substring(0, encryptedPassword.length() - 1);
    }

    /**
     * 암호화되지 않은 패스워드와 암호화된 패스워드를 복호화한 값이 동일한지 여부를 리턴한다.
     *
     * @param unencryptedPassword 암호화되지 않은 패스워드
     * @param encryptedPassword   암호화된 패스워드
     * @return 같으면 true, 다르면 false
     */
    public static boolean isMatchPassword(String unencryptedPassword, String encryptedPassword) {
        return Objects.equals(unencryptedPassword, decrypt(encryptedPassword));
    }
}
