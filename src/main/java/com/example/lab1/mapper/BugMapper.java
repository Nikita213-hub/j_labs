package com.example.lab1.mapper;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.Comment;
import com.example.lab1.domain.Project;
import com.example.lab1.domain.Tag;
import com.example.lab1.domain.UserAccount;
import com.example.lab1.dto.BugDetailsDto;
import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.CommentDto;
import com.example.lab1.dto.ProjectDto;
import com.example.lab1.dto.UserSummaryDto;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
                bug.getProject() != null ? bug.getProject().getCode() : null,
                username(bug.getReporter()),
                username(bug.getAssignee()),
                bug.getCreatedAt(),
                bug.getUpdatedAt()
        );
    }

    public BugDetailsDto toDetails(Bug bug) {
        return new BugDetailsDto(
                bug.getId(),
                bug.getTitle(),
                bug.getDescription(),
                bug.getStatus(),
                bug.getPriority(),
                toProject(bug.getProject()),
                toUser(bug.getReporter()),
                toUser(bug.getAssignee()),
                mapTags(bug.getTags()),
                mapComments(bug.getComments()),
                bug.getCreatedAt(),
                bug.getUpdatedAt()
        );
    }

    private List<String> mapTags(Set<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .sorted()
                .toList();
    }

    private List<CommentDto> mapComments(List<Comment> comments) {
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(this::toComment)
                .toList();
    }

    private CommentDto toComment(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getBody(),
                comment.getAuthor() != null ? comment.getAuthor().getId() : null,
                username(comment.getAuthor()),
                comment.getCreatedAt()
        );
    }

    private ProjectDto toProject(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectDto(project.getId(), project.getCode(), project.getName());
    }

    private UserSummaryDto toUser(UserAccount account) {
        if (account == null) {
            return null;
        }
        return new UserSummaryDto(account.getId(), account.getUsername(), account.getRole());
    }

    private String username(UserAccount account) {
        return account != null ? account.getUsername() : null;
    }
}
