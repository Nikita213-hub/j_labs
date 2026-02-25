package com.example.lab1.controller;

import com.example.lab1.domain.BugPriority;
import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.BugRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BugControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnBugsFilteredByStatusRequestParam() throws Exception {
        mockMvc.perform(get("/api/v1/bugs").param("status", "open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].status", Matchers.everyItem(Matchers.equalTo("OPEN"))));
    }

    @Test
    void shouldCreateAndFetchBugUsingPathVariable() throws Exception {
        BugRequest request = new BugRequest(
                "API responds 500 when payload empty",
                "POST /api/v1/bugs with empty body returns 500 instead of 400.",
                BugPriority.HIGH,
                "qa.sam",
                "dev.jane"
        );
        String payload = objectMapper.writeValueAsString(request);

        MvcResult createResult = mockMvc.perform(post("/api/v1/bugs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn();

        BugDto created = objectMapper.readValue(createResult.getResponse().getContentAsString(), BugDto.class);
        assertThat(created.id()).isNotNull();

        mockMvc.perform(get("/api/v1/bugs/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("API responds 500 when payload empty"));
    }
}
