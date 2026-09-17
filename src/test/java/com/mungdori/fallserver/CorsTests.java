package com.mungdori.fallserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "app.frontend-url=http://localhost:5173")
@AutoConfigureMockMvc
class CorsTests {
    @Autowired MockMvc mvc;

    @Test void allowsFrontendPreflight() throws Exception {
        mvc.perform(options("/api/member/login")
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "content-type,authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().exists("Access-Control-Allow-Headers"));
    }

    @Test void rejectsOtherOrigins() throws Exception {
        mvc.perform(options("/api/member/login")
                .header("Origin", "http://localhost:9999")
                .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test void includesCorsHeadersOnValidationErrors() throws Exception {
        mvc.perform(post("/api/member/login")
                .header("Origin", "http://localhost:5173")
                .contentType("application/json").content("{\"code\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }
}
