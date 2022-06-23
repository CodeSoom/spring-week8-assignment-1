package com.codesoom.assignment.docs.common;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemProvider {

    private List<Item> items = new ArrayList<>();

    public ItemProvider(Item... items) {
        Arrays.stream(items)
                .forEach(item -> this.items.add(item));
    }

    public List<FieldDescriptor> toFields() {
        List<FieldDescriptor> list = new ArrayList<>();

        for (Item i : this.items) {
            list.add(i.toField());
        }

        return list;
    }

    public List<HeaderDescriptor> toHeaders() {
        List<HeaderDescriptor> list = new ArrayList<>();

        for (Item i : this.items) {
            list.add(i.toHeader());
        }

        return list;
    }

    public List<ParameterDescriptor> toParameters() {
        List<ParameterDescriptor> list = new ArrayList<>();

        for (Item i : this.items) {
            list.add(i.toParameter());
        }

        return list;
    }
}
