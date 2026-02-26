package com.example.lab1.mapper;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugStatus;
import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.BugRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BugMapper {

    public BugDto toDto(Bug bug) {
        return new BugDto(
                bug.getId(),
                bug.getTitle(),
                bug.getDescription(),
                bug.getStatus(),
                bug.getPriority(),
                bug.getReporter(),
                bug.getAssignee(),
                bug.getCreatedAt(),
                bug.getUpdatedAt()
        );
    }

    public Bug toEntity(BugRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return Bug.builder()
                .title(request.title())
                .description(request.description())
                .status(BugStatus.OPEN)
                .priority(request.priority())
                .reporter(request.reporter())
                .assignee(request.assignee())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public List<BugDto> toDtoList(List<Bug> bugs) {
        return bugs.stream().map(this::toDto).toList();
    }
}
