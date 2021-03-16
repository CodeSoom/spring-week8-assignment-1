package com.codesoom.assignment.global.config.security.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 문자 인코딩 설정 필터.
 */
@Component
public class CharacterFilter {
    private CharacterEncodingFilter characterEncodingFilter;

    public CharacterFilter() {
        this.characterEncodingFilter = new CharacterEncodingFilter();
    }

    public CharacterEncodingFilter getFilter() {
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
