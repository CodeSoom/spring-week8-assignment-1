package com.codesoom.assignment.utils;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;

@AutoConfigureRestDocs
@Import(RestDocTestConfiguration.class)
public class RestDocTestBase {
}
