package com.codesoom.assignment.support.test;

import java.util.HashMap;
import java.util.Map;

public class FieldsItemResponse {
    private final Map<String, Object> member = new HashMap<>();

    public FieldsItemResponse() {
        Map<String, Object> music = new HashMap<>();
        music.put("artist", "Snowman");
        music.put("title", "Sia");

        Map<String, Object> movie = new HashMap<>();
        movie.put("name", "Coco");
        movie.put("star", 5);

        Map<String, Object> favorite = new HashMap<>();
        favorite.put("movie", movie);
        favorite.put("music", music);

        member.put("id", 1);
        member.put("name", "홍길동");
        member.put("favorite", favorite);
    }

    public Map<String, Object> getMember() {
        return member;
    }
}
