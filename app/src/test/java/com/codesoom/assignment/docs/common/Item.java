package com.codesoom.assignment.docs.common;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public class Item {

    private String path;

    private JsonFieldType type;

    private String description;

    private State state = State.NONE;

    private Item(String path, String description) {
        this.path = path;
        this.description = description;
    }

    private Item(String path, JsonFieldType type, String description) {
        this.path = path;
        this.type = type;
        this.description = description;
    }

    public Item(String path, JsonFieldType type, String description, State state) {
        this.path = path;
        this.type = type;
        this.description = description;
        this.state = state;
    }

    public static Item of(String path, String description) {
        return new Item(path, description);
    }

    public static Item of(String path, JsonFieldType type, String description) {
        return new Item(path, type, description);
    }

    public static Item of(String path, JsonFieldType type, String description, State state) {
        return new Item(path, type, description, state);
    }

    public HeaderDescriptor toHeader() {
        HeaderDescriptor headerDescriptor = headerWithName(this.path)
                .description(this.description);

        switch (this.state) {
            case OPTIONAL:
                return headerDescriptor.optional();
            default:
                return headerDescriptor;
        }
    }

    public ParameterDescriptor toParameter() {
        ParameterDescriptor parameterDescriptor = parameterWithName(this.path)
                .description(this.description);

        return parameterDescriptor;
    }

    public FieldDescriptor toField() {
        FieldDescriptor fieldDescriptor = fieldWithPath(this.path)
                .type(this.type)
                .description((this.description));

        switch (this.state) {
            case IGNORED:
                return fieldDescriptor.ignored();
            case OPTIONAL:
                return fieldDescriptor.optional();
            default:
                return fieldDescriptor;
        }
    }
}
