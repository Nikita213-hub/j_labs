package com.example.lab1.repository;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BugRepository extends JpaRepository<Bug, UUID> {

    List<Bug> findByStatus(BugStatus status);

    @EntityGraph(attributePaths = {"project", "reporter", "assignee", "tags", "comments", "comments.author"})
    @Query("select b from Bug b")
    List<Bug> findAllWithAssociations();
}
