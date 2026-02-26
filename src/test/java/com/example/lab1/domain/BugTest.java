package com.example.lab1.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BugTest {

    @Test
    void constructorShouldAssignAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        Bug bug = new Bug(
                id,
                "Null pointer when saving",
                "Reproduce via POST /bugs with empty description",
                BugStatus.IN_PROGRESS,
                BugPriority.HIGH,
                "qa.nina",
                "dev.oleg",
                created,
                updated
        );

        assertThat(bug.getId()).isEqualTo(id);
        assertThat(bug.getTitle()).isEqualTo("Null pointer when saving");
        assertThat(bug.getDescription()).contains("POST /bugs");
        assertThat(bug.getStatus()).isEqualTo(BugStatus.IN_PROGRESS);
        assertThat(bug.getPriority()).isEqualTo(BugPriority.HIGH);
        assertThat(bug.getReporter()).isEqualTo("qa.nina");
        assertThat(bug.getAssignee()).isEqualTo("dev.oleg");
        assertThat(bug.getCreatedAt()).isEqualTo(created);
        assertThat(bug.getUpdatedAt()).isEqualTo(updated);
    }
}
