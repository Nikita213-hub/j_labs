package com.example.lab1.config;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugPriority;
import com.example.lab1.domain.BugStatus;
import com.example.lab1.domain.Comment;
import com.example.lab1.domain.Project;
import com.example.lab1.domain.Tag;
import com.example.lab1.domain.UserAccount;
import com.example.lab1.domain.UserRole;
import com.example.lab1.repository.BugRepository;
import com.example.lab1.repository.ProjectRepository;
import com.example.lab1.repository.TagRepository;
import com.example.lab1.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final BugRepository bugRepository;

    public DatabaseSeeder(
            UserAccountRepository userAccountRepository,
            ProjectRepository projectRepository,
            TagRepository tagRepository,
            BugRepository bugRepository
    ) {
        this.userAccountRepository = userAccountRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.bugRepository = bugRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (bugRepository.count() > 0) {
            return;
        }
        UserAccount qa = createUser("qa.olga", UserRole.QA);
        UserAccount dev = createUser("dev.sergey", UserRole.DEVELOPER);
        UserAccount manager = createUser("pm.liza", UserRole.MANAGER);

        Project project = new Project();
        project.setCode("BUGS");
        project.setName("Bug Tracker Portal");
        project.setDescription("Reference domain model for lab one");
        projectRepository.save(project);

        Tag backend = createTag("backend");
        Tag ui = createTag("ui");

        Bug bug = new Bug();
        bug.setTitle("Dark mode breaks validation");
        bug.setDescription("Text inputs hard to read in dark theme");
        bug.setStatus(BugStatus.IN_PROGRESS);
        bug.setPriority(BugPriority.HIGH);
        bug.setProject(project);
        bug.setReporter(qa);
        bug.setAssignee(dev);
        bug.addTag(backend);
        bug.addTag(ui);

        Comment first = new Comment();
        first.setBody("Reproduced on Chrome 119 and Firefox 120");
        first.setAuthor(qa);
        bug.addComment(first);

        Comment answer = new Comment();
        answer.setBody("Working on CSS variables fix");
        answer.setAuthor(dev);
        bug.addComment(answer);

        bugRepository.save(bug);

        Bug second = new Bug();
        second.setTitle("Cannot add watchers");
        second.setDescription("500 error when QA adds more than 3 watchers");
        second.setStatus(BugStatus.OPEN);
        second.setPriority(BugPriority.CRITICAL);
        second.setProject(project);
        second.setReporter(manager);
        second.setAssignee(dev);
        second.addTag(backend);
        bugRepository.save(second);
    }

    private UserAccount createUser(String username, UserRole role) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setRole(role);
        return userAccountRepository.save(account);
    }

    private Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }
}
