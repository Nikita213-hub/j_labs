package com.example.lab1.repository;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BugRepository {

    List<Bug> findAll();

    List<Bug> findByStatus(BugStatus status);

    Optional<Bug> findById(UUID id);

    Bug save(Bug bug);
}
