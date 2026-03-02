package com.example.lab1.service;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.BugStatus;
import com.example.lab1.domain.Project;
import com.example.lab1.domain.UserAccount;
import com.example.lab1.dto.BulkProjectRequest;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.repository.BugRepository;
import com.example.lab1.repository.ProjectRepository;
import com.example.lab1.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BulkProjectService {

    private final ProjectRepository projectRepository;
    private final BugRepository bugRepository;
    private final UserAccountRepository userAccountRepository;

    public BulkProjectService(
            ProjectRepository projectRepository,
            BugRepository bugRepository,
            UserAccountRepository userAccountRepository
    ) {
        this.projectRepository = projectRepository;
        this.bugRepository = bugRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public void saveWithoutTransaction(BulkProjectRequest request) {
        persist(request);
    }

    @Transactional
    public void saveWithTransaction(BulkProjectRequest request) {
        persist(request);
    }

    private void persist(BulkProjectRequest request) {
        Project project = projectRepository.findByCode(request.projectCode())
                .orElseGet(() -> projectRepository.save(buildProject(request)));
        project.setName(request.projectName());
        project.setDescription(request.description());
        projectRepository.save(project);

        UserAccount reporter = fetchUser(request.reporterId());
        UserAccount assignee = request.assigneeId() != null ? fetchUser(request.assigneeId()) : null;

        for (int i = 0; i < request.bugTitles().size(); i++) {
            String title = request.bugTitles().get(i);
            Bug bug = new Bug();
            bug.setTitle(title);
            bug.setDescription("Bulk created bug: " + title);
            bug.setPriority(BugPriority.MEDIUM);
            bug.setStatus(BugStatus.OPEN);
            bug.setProject(project);
            bug.setReporter(reporter);
            bug.setAssignee(assignee);
            bugRepository.save(bug);
            if (request.failAfterFirstBug() && i == 0) {
                throw new IllegalStateException("Simulated failure after the first bug");
            }
        }
    }

    private Project buildProject(BulkProjectRequest request) {
        Project project = new Project();
        project.setCode(request.projectCode());
        project.setName(request.projectName());
        project.setDescription(request.description());
        return project;
    }

    private UserAccount fetchUser(Long userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
