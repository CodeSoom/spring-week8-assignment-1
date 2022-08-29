package com.codesoom.assignment.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
