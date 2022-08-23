package com.codesoom.assignment.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EncryptionUtilTest {
    @Test
    @DisplayName("주어진 패스워드와 그 패스워드를 암호화한 패스워드를 복호화한 결과는 같다.")
    void ResultOfDecryptingAnUnencryptedPasswordAndAnEncryptedPasswordIsSame() {
        // given
        String unencryptedPassword = "Abcde1234!";
        String encryptedPassword = EncryptionUtil.encrypt(unencryptedPassword);
        // when
        boolean result = EncryptionUtil.isMatchPassword(unencryptedPassword, encryptedPassword);
        // then
        assertTrue(result);
    }

    private static class EncryptionUtil {
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
         * @param encryptedPassword 암호화된 패스워드
         * @return 같으면 true, 다르면 false
         */
        public static boolean isMatchPassword(String unencryptedPassword, String encryptedPassword) {
            return Objects.equals(unencryptedPassword, decrypt(encryptedPassword));
        }
    }
}
