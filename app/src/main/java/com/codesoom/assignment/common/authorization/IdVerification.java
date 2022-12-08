package com.codesoom.assignment.common.authorization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Access Token의 id와 요청 데이터(@PathVariable)의 id를 비교해야 할 때 사용됩니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdVerification {
}
