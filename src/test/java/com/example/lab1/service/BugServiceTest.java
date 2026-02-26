package com.example.lab1.service;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.BugStatus;
import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.BugRequest;
import com.example.lab1.exception.BugNotFoundException;
import com.example.lab1.mapper.BugMapper;
import com.example.lab1.repository.BugRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BugServiceTest {

    @Mock
    private BugRepository bugRepository;

    @Mock
    private BugMapper bugMapper;

    @InjectMocks
    private BugService bugService;

    @Test
    void shouldReturnBugsByStatus() {
        Bug bug = Bug.builder()
                .id(UUID.randomUUID())
                .title("Crash on start")
                .description("Null pointer.")
                .status(BugStatus.OPEN)
                .priority(BugPriority.CRITICAL)
                .reporter("qa.ann")
                .assignee("dev.max")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(bugRepository.findByStatus(BugStatus.OPEN)).thenReturn(List.of(bug));
        BugDto dto = new BugDto(
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
        when(bugMapper.toDtoList(List.of(bug))).thenReturn(List.of(dto));

        List<BugDto> result = bugService.findBugs("open");

        assertThat(result).containsExactly(dto);
    }

    @Test
    void shouldThrowWhenBugMissing() {
        UUID id = UUID.randomUUID();
        when(bugRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bugService.findById(id))
                .isInstanceOf(BugNotFoundException.class);
    }

    @Test
    void shouldCreateBugFromRequest() {
        BugRequest request = new BugRequest(
                "UI glitch",
                "Buttons overlap on mobile.",
                BugPriority.MEDIUM,
                "qa.kat",
                "dev.art"
        );
        Bug entity = Bug.builder()
                .title(request.title())
                .description(request.description())
                .status(BugStatus.OPEN)
                .priority(request.priority())
                .reporter(request.reporter())
                .assignee(request.assignee())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Bug saved = Bug.builder()
                .id(UUID.randomUUID())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .reporter(entity.getReporter())
                .assignee(entity.getAssignee())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        BugDto dto = new BugDto(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStatus(),
                saved.getPriority(),
                saved.getReporter(),
                saved.getAssignee(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );

        when(bugMapper.toEntity(request)).thenReturn(entity);
        when(bugRepository.save(entity)).thenReturn(saved);
        when(bugMapper.toDto(saved)).thenReturn(dto);

        BugDto result = bugService.create(request);

        assertThat(result).isEqualTo(dto);
    }
}
