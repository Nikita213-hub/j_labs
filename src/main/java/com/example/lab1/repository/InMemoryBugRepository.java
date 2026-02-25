package com.example.lab1.repository;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.BugStatus;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBugRepository implements BugRepository {

    private final Map<UUID, Bug> storage = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadSampleData() {
        save(sample("Crash on login", """
                When user enters invalid password twice the client crashes with NullPointerException.
                """, BugStatus.OPEN, BugPriority.CRITICAL, "qa.amy", "dev.bob"));

        save(sample("Broken dark theme", """
                Several form fields keep default colors in dark theme (settings page, user modal).
                """, BugStatus.IN_PROGRESS, BugPriority.MEDIUM, "qa.lena", "dev.ivan"));

        save(sample("Report export typo", """
                CSV export uses 'Seperator' column header; should be spelled 'Separator'.
                """, BugStatus.RESOLVED, BugPriority.LOW, "qa.sara", "tech.writer"));
    }

    private Bug sample(String title, String description, BugStatus status, BugPriority priority,
                       String reporter, String assignee) {
        LocalDateTime now = LocalDateTime.now();
        return new Bug(
                UUID.randomUUID(),
                title,
                description,
                status,
                priority,
                reporter,
                assignee,
                now.minusDays(2),
                now.minusHours(5)
        );
    }

    @Override
    public List<Bug> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Bug> findByStatus(BugStatus status) {
        return storage.values().stream()
                .filter(bug -> bug.getStatus() == status)
                .toList();
    }

    @Override
    public Optional<Bug> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Bug save(Bug bug) {
        UUID bugId = bug.getId() == null ? UUID.randomUUID() : bug.getId();
        bug.setId(bugId);
        LocalDateTime now = LocalDateTime.now();
        if (bug.getCreatedAt() == null) {
            bug.setCreatedAt(now);
        }
        bug.setUpdatedAt(now);
        storage.put(bugId, bug);
        return bug;
    }
}
