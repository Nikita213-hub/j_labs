package com.example.lab1.repository;

import com.example.lab1.domain.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByIdIn(Iterable<Long> ids);
}
