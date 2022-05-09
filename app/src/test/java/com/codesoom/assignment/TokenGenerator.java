package com.codesoom.assignment;

import com.codesoom.assignment.dto.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TokenGenerator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateToken(MockMvc mockMvc, String email, String password) throws Exception {
        final byte[] contentAsByteArray = mockMvc.perform(post("/session")
                        .content(String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsByteArray();
        final TokenResponse tokenResponse = objectMapper.readValue(contentAsByteArray, TokenResponse.class);
        return tokenResponse.getAccessToken();
    }

}
