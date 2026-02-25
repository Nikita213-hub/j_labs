package com.example.lab1.controller;

import com.example.lab1.dto.BookDto;
import com.example.lab1.dto.BookRequest;
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
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnBooksFilteredByAuthorRequestParam() throws Exception {
        mockMvc.perform(get("/api/books").param("author", "martin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author", Matchers.containsStringIgnoringCase("martin")));
    }

    @Test
    void shouldCreateAndFetchBookUsingPathVariable() throws Exception {
        BookRequest request = new BookRequest("Test Driven Development", "Kent Beck", 2003);
        String payload = objectMapper.writeValueAsString(request);

        MvcResult createResult = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        BookDto created = objectMapper.readValue(createResult.getResponse().getContentAsString(), BookDto.class);
        assertThat(created.id()).isNotNull();

        mockMvc.perform(get("/api/books/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Driven Development"));
    }
}
