package com.codesoom.assignment.support;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.Assert;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class FieldsItem {

    private String path;
    private JsonFieldType type;
    private String description;
    private List<FieldsItem> children = new ArrayList<>();

    private FieldsItem(String path, JsonFieldType type, String description) {
        this.path = path;
        this.type = type;
        this.description = description;
    }

    private FieldsItem(String path, JsonFieldType type, String description, List<FieldsItem> items) {
        this.path = path;
        this.type = type;
        this.description = description;
        this.children = items;
    }

    public static FieldsItem of(String path, JsonFieldType type, String description) {
        Assert.notNull(path, "path는 필수값입니다");
        Assert.notNull(type, "type은 필수값입니다");
        Assert.notNull(description, "description은 필수값입니다");

        return new FieldsItem(path, type, description);
    }

    public static FieldsItem of(String path, JsonFieldType type, String description, FieldsItem... items) {
        Assert.notNull(path, "path는 필수값입니다");
        Assert.notNull(type, "type은 필수값입니다");
        Assert.notNull(description, "description은 필수값입니다");
        Assert.notNull(items, "item은 필수값입니다");

        return new FieldsItem(path, type, description, List.of(items));
    }

    /**
     * FieldDescriptor로 변환합니다.
     */
    public FieldDescriptor toField() {
        return fieldWithPath(this.path)
                .type(this.type)
                .description(this.description);
    }

    public List<FieldDescriptor> build() {
        return recursiveBuild();
    }

    /**
     * 반복을 사용해 FieldDescriptor 목록으로 변환합니다.
     */
    public List<FieldDescriptor> iterableBuild() {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

        // 최상위 필드를 담는다.
        fieldDescriptors.add(
                fieldWithPath(this.path)
                        .type(this.type)
                        .description(this.description)
        );

        // 자식 필드가 없을 때까지 반복하며 결과 목록에 추가한다.
        Deque<FieldChildInfo> queue = new ArrayDeque<>();
        queue.add(new FieldChildInfo(this.children, this.path));
        while (!queue.isEmpty()) {
            FieldChildInfo current = queue.poll();

            for (FieldsItem child : current.children) {
                fieldDescriptors.add(
                        fieldWithPath(current.parentPath + "." + child.path)
                                .type(child.type)
                                .description(child.description)
                );

                // 자식의 자식 리스트가 있다면 대기 큐에 추가한다.
                if (!child.children.isEmpty()) {
                    queue.add(new FieldChildInfo(child.children, current.parentPath + "." + child.path));
                }
            }
        }

        return fieldDescriptors;
    }

    /**
     * 자식을 재귀적으로 호출하며 결과 목록에 추가합니다.
     */
    private List<FieldDescriptor> getChildList(String parentPath, List<FieldsItem> children) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

        for (FieldsItem child : children) {
            fieldDescriptors.add(fieldWithPath(parentPath + "." + child.path)
                    .type(child.type)
                    .description(child.description));

            // 자식이 자식 리스트를 갖는 경우 재귀 호출
            if (!child.children.isEmpty()) {
                fieldDescriptors.addAll(getChildList(parentPath + "." + child.path, child.children));
            }
        }

        return fieldDescriptors;
    }

    /**
     * 재귀를 사용해 FieldDescriptor 목록으로 변환합니다.
     */
    public List<FieldDescriptor> recursiveBuild() {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

        // 최상위 필드를 담는다.
        fieldDescriptors.add(
                fieldWithPath(this.path)
                        .type(this.type)
                        .description(this.description)
        );

        // 자식 필드가 있으면 결과 목록에 추가한다.
        for (FieldsItem child : children) {
            fieldDescriptors.add(
                    fieldWithPath(this.path + "." + child.path)
                            .type(child.type)
                            .description(child.description)
            );

            // 자식의 자식이 있으면 목록에 추가한다.
            if (!child.children.isEmpty()) {
                fieldDescriptors.addAll(getChildList(this.path + "." + child.path, child.children));
            }
        }

        return fieldDescriptors;
    }

    /**
     * iterableBuild 메소드에서 사용하기 위해 정의한 클래스로,
     * 자식 목록과 부모 경로를 갖습니다.
     */
    private static class FieldChildInfo {
        List<FieldsItem> children;
        String parentPath;

        public FieldChildInfo(List<FieldsItem> children, String parentPath) {
            this.children = children;
            this.parentPath = parentPath;
        }
    }
}
