package com.codesoom.assignment.support;

public enum IdFixture {
    ID_1(1L),
    ID_2(2L),
    ID_1004(1004L),
    ID_MAX(Long.MAX_VALUE),
    ;

    private final Long id;

    IdFixture(Long id) {
        this.id = id;
    }

    public Long value() {
        return id;
    }
}
