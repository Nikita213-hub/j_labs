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
        Bug bug = new Bug(
                UUID.randomUUID(),
                "Crash on start",
                "Null pointer.",
                BugStatus.OPEN,
                BugPriority.CRITICAL,
                "qa.ann",
                "dev.max",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
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
        Bug entity = new Bug(
                null,
                request.title(),
                request.description(),
                BugStatus.OPEN,
                request.priority(),
                request.reporter(),
                request.assignee(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Bug saved = new Bug(
                UUID.randomUUID(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getPriority(),
                entity.getReporter(),
                entity.getAssignee(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
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
