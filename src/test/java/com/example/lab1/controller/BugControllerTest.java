package com.example.lab1.controller;

import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.Project;
import com.example.lab1.domain.Tag;
import com.example.lab1.domain.UserAccount;
import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.BugRequest;
import com.example.lab1.repository.ProjectRepository;
import com.example.lab1.repository.TagRepository;
import com.example.lab1.repository.UserAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private TagRepository tagRepository;

    private Long projectId;
    private Long reporterId;
    private Long assigneeId;
    private Long tagId;

    @BeforeEach
    void setUp() {
        Project project = projectRepository.findAll().get(0);
        projectId = project.getId();
        List<UserAccount> users = userAccountRepository.findAll();
        reporterId = users.get(0).getId();
        assigneeId = users.get(1).getId();
        Tag tag = tagRepository.findAll().get(0);
        tagId = tag.getId();
    }

    @Test
    void shouldReturnBugsFilteredByStatusRequestParam() throws Exception {
        mockMvc.perform(get("/api/v1/bugs").param("status", "open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].status", Matchers.everyItem(Matchers.equalTo("OPEN"))));
    }

    @Test
    void shouldCreateFetchAndListDetailedBug() throws Exception {
        BugRequest request = new BugRequest(
                "API responds 500 when payload empty",
                "POST /api/v1/bugs with empty body returns 500 instead of 400.",
                BugPriority.HIGH,
                null,
                projectId,
                reporterId,
                assigneeId,
                Set.of(tagId)
        );

        String payload = objectMapper.writeValueAsString(request);

        MvcResult createResult = mockMvc.perform(post("/api/v1/bugs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.projectCode").isNotEmpty())
                .andReturn();

        BugDto created = objectMapper.readValue(createResult.getResponse().getContentAsString(), BugDto.class);

        mockMvc.perform(get("/api/v1/bugs/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("API responds 500 when payload empty"));

        mockMvc.perform(get("/api/v1/bugs/{id}/details", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.project.code").isNotEmpty())
                .andExpect(jsonPath("$.tags").isArray());
    }

    @Test
    void shouldReturnDetailedListWithAndWithoutOptimization() throws Exception {
        mockMvc.perform(get("/api/v1/bugs/details").param("optimized", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comments").isArray());

        mockMvc.perform(get("/api/v1/bugs/details").param("optimized", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].project").exists());
    }
}
