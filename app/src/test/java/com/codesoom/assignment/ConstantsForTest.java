package com.codesoom.assignment;

import java.util.List;
import java.util.stream.Collectors;

public class ConstantsForTest {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String VALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.cWmYWcQTBqTVGz8syjb9hgBTAa9c4Qs-zHN_KxL5Rl8";
    public static final List<String> INVALID_TOKENS = List.of(
            ""
            , " "
            , "112345678904162398746928372829829"
            , "eyJhbGciOiJIUzI1NiJ9"
            , "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9"
            , "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ze4dJmmF4peSe1uo9-ug019VAwzhr0WO8H3iHroSOUi"
            , "한글로토큰이만들어지지않습니다"
            , "한글로.토큰이.만들어지지않습니다"
            , "eyJhbGciOiJIUzI1NiJ9eyJhbGciOiJIUzI1NiJ9eyJhbGciOiJIUzI1NiJdlksiei34828.398274198230dlkj" +
                    "seif21932432.9348723riosw;fkajdslkfi8w03249283lkkdsdflkjsielskd"
            , "1!@#$@#$%&^%$#$23398282#$@#%@%@##@$.3987432$@#$290233.@#$*&@#)(*$O)@(#")
            .stream().map(str -> TOKEN_PREFIX + str)
            .collect(Collectors.toList());

}
