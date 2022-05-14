package com.codesoom.assignment.controller;

import org.apache.logging.log4j.util.Strings;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class Item {

    String path = "";
    JsonFieldType type;
    String description = "";
    List<Item> children = new ArrayList<>();

    public Item(String path, JsonFieldType type, String description, List<Item> children) {
        this.path = path;
        this.type = type;
        this.description = description;
        this.children = children;
    }

    public Item(String path, JsonFieldType type, String description) {
        this.path = path;
        this.type = type;
        this.description = description;
    }

    public Item(String path, List<Item> children) {
        this.path = path;
        this.children = children;
    }

    static Item of(String path, JsonFieldType type, String description) {
        return new Item(path, type, description);
    }

    public static Item of(String path, JsonFieldType type, String description, Item... children) {
        return new Item(path, type, description, Arrays.asList(children));
    }

    static Item of(String path, Item... children) {
        return new Item(path, Arrays.asList(children));
    }

    public FieldDescriptor toField() {
        FieldDescriptor fieldDescriptor = fieldWithPath(this.path)
                .type(this.type)
                .description(this.description);

        return fieldDescriptor;
    }

    public List<Item> toFlatList(String superPath) {
        List<Item> list = new ArrayList<>();
        if (!Strings.isBlank(superPath)) {
            this.path = superPath + "." + this.path;
        }

        if (this.type != null || this.children == null || this.children.size() < 1) {
            list.add(this);
        }

        for (Item child : children) {
            list.addAll(child.toFlatList(this.path));
        }

        return list;
    }

    public List<Item> toFlatList() {
        return toFlatList("");
    }

    public List<FieldDescriptor> build() {
        List<FieldDescriptor> list = new ArrayList<>();
        for (Item i : toFlatList()) {
            list.add(i.toField());
        }

        return list;
    }

}
