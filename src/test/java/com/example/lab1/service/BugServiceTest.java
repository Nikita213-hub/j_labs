package com.example.lab1.service;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.BugStatus;
import com.example.lab1.domain.Project;
import com.example.lab1.domain.Tag;
import com.example.lab1.domain.UserAccount;
import com.example.lab1.domain.UserRole;
import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.BugRequest;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.BugMapper;
import com.example.lab1.repository.BugRepository;
import com.example.lab1.repository.ProjectRepository;
import com.example.lab1.repository.TagRepository;
import com.example.lab1.repository.UserAccountRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BugServiceTest {

    @Mock
    private BugRepository bugRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private TagRepository tagRepository;

    private BugService bugService;

    @BeforeEach
    void setUp() {
        bugService = new BugService(bugRepository, projectRepository, userAccountRepository, tagRepository, new BugMapper());
    }

    @Test
    void shouldFindBugsByStatusFilter() {
        Bug bug = buildBug();
        bug.setStatus(BugStatus.OPEN);
        when(bugRepository.findByStatus(BugStatus.OPEN)).thenReturn(List.of(bug));

        List<BugDto> result = bugService.findBugs("open");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(BugStatus.OPEN);
    }

    @Test
    void shouldCreateBugAndResolveRelations() {
        BugRequest request = new BugRequest(
                "Cannot login",
                "OAuth callback broken",
                BugPriority.HIGH,
                null,
                1L,
                2L,
                3L,
                Set.of(10L)
        );
        Project project = project();
        UserAccount reporter = user("qa", UserRole.QA);
        UserAccount assignee = user("dev", UserRole.DEVELOPER);
        Tag tag = tag("backend");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userAccountRepository.findById(2L)).thenReturn(Optional.of(reporter));
        when(userAccountRepository.findById(3L)).thenReturn(Optional.of(assignee));
        when(tagRepository.findByIdIn(Set.of(10L))).thenReturn(List.of(tag));
        when(bugRepository.save(org.mockito.ArgumentMatchers.any(Bug.class))).thenAnswer(invocation -> {
            Bug toSave = invocation.getArgument(0);
            toSave.setId(UUID.randomUUID());
            return toSave;
        });

        BugDto created = bugService.create(request);

        assertThat(created.status()).isEqualTo(BugStatus.OPEN);
        ArgumentCaptor<Bug> captor = ArgumentCaptor.forClass(Bug.class);
        verify(bugRepository).save(captor.capture());
        Bug saved = captor.getValue();
        assertThat(saved.getProject()).isEqualTo(project);
        assertThat(saved.getReporter()).isEqualTo(reporter);
        assertThat(saved.getAssignee()).isEqualTo(assignee);
        assertThat(saved.getTags()).extracting(Tag::getName).containsExactly("backend");
    }

    @Test
    void shouldThrowIfBugMissing() {
        UUID id = UUID.randomUUID();
        when(bugRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bugService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUseEntityGraphWhenOptimized() {
        when(bugRepository.findAllWithAssociations()).thenReturn(List.of(buildBug()));

        bugService.findDetailedBugs(true);

        verify(bugRepository).findAllWithAssociations();
        verifyNoInteractions(projectRepository, userAccountRepository, tagRepository);
    }

    private Bug buildBug() {
        Bug bug = new Bug();
        bug.setTitle("Sample");
        bug.setDescription("Sample description");
        bug.setPriority(BugPriority.MEDIUM);
        bug.setStatus(BugStatus.OPEN);
        bug.setProject(project());
        bug.setReporter(user("qa", UserRole.QA));
        bug.setAssignee(user("dev", UserRole.DEVELOPER));
        return bug;
    }

    private Project project() {
        Project project = new Project();
        project.setCode("LAB");
        project.setName("Lab Project");
        return project;
    }

    private UserAccount user(String username, UserRole role) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setRole(role);
        return account;
    }

    private Tag tag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }
}
