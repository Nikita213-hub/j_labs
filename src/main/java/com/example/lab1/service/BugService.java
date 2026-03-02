package com.example.lab1.service;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugStatus;
import com.example.lab1.domain.Project;
import com.example.lab1.domain.Tag;
import com.example.lab1.domain.UserAccount;
import com.example.lab1.dto.BugDetailsDto;
import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.BugRequest;
import com.example.lab1.exception.ResourceNotFoundException;
import com.example.lab1.mapper.BugMapper;
import com.example.lab1.repository.BugRepository;
import com.example.lab1.repository.ProjectRepository;
import com.example.lab1.repository.TagRepository;
import com.example.lab1.repository.UserAccountRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class BugService {

    private final BugRepository bugRepository;
    private final ProjectRepository projectRepository;
    private final UserAccountRepository userAccountRepository;
    private final TagRepository tagRepository;
    private final BugMapper bugMapper;

    public BugService(
            BugRepository bugRepository,
            ProjectRepository projectRepository,
            UserAccountRepository userAccountRepository,
            TagRepository tagRepository,
            BugMapper bugMapper
    ) {
        this.bugRepository = bugRepository;
        this.projectRepository = projectRepository;
        this.userAccountRepository = userAccountRepository;
        this.tagRepository = tagRepository;
        this.bugMapper = bugMapper;
    }

    @Transactional(readOnly = true)
    public List<BugDto> findBugs(String statusFilter) {
        List<Bug> bugs = StringUtils.hasText(statusFilter)
                ? bugRepository.findByStatus(resolveStatus(statusFilter))
                : bugRepository.findAll();
        return bugs.stream().map(bugMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BugDto findById(UUID id) {
        Bug bug = fetchBug(id);
        return bugMapper.toDto(bug);
    }

    @Transactional(readOnly = true)
    public BugDetailsDto findDetails(UUID id) {
        Bug bug = fetchBug(id);
        return bugMapper.toDetails(bug);
    }

    @Transactional(readOnly = true)
    public List<BugDetailsDto> findDetailedBugs(boolean optimized) {
        List<Bug> bugs = optimized
                ? bugRepository.findAllWithAssociations()
                : bugRepository.findAll();
        return bugs.stream().map(bugMapper::toDetails).toList();
    }

    @Transactional
    public BugDto create(BugRequest request) {
        Bug bug = new Bug();
        applyRequest(bug, request);
        Bug saved = bugRepository.save(bug);
        return bugMapper.toDto(saved);
    }

    @Transactional
    public BugDto update(UUID id, BugRequest request) {
        Bug bug = fetchBug(id);
        applyRequest(bug, request);
        return bugMapper.toDto(bug);
    }

    @Transactional
    public void delete(UUID id) {
        Bug bug = fetchBug(id);
        bugRepository.delete(bug);
    }

    private Bug fetchBug(UUID id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bug", id));
    }

    private void applyRequest(Bug bug, BugRequest request) {
        bug.setTitle(request.title());
        bug.setDescription(request.description());
        bug.setPriority(request.priority());
        bug.setStatus(request.status() != null ? request.status() : defaultStatus(bug));
        bug.setProject(findProject(request.projectId()));
        bug.setReporter(findUser(request.reporterId()));
        bug.setAssignee(request.assigneeId() != null ? findUser(request.assigneeId()) : null);
        updateTags(bug, request.tagIds());
    }

    private BugStatus defaultStatus(Bug bug) {
        return bug.getStatus() != null ? bug.getStatus() : BugStatus.OPEN;
    }

    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
    }

    private UserAccount findUser(Long userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    private void updateTags(Bug bug, Set<Long> tagIds) {
        if (tagIds == null) {
            return;
        }
        bug.getTags().clear();
        if (CollectionUtils.isEmpty(tagIds)) {
            return;
        }
        Set<Tag> tags = new HashSet<>(tagRepository.findByIdIn(tagIds));
        if (tags.size() != tagIds.size()) {
            throw new ResourceNotFoundException("Tag", tagIds);
        }
        tags.forEach(bug::addTag);
    }

    private BugStatus resolveStatus(String status) {
        try {
            return BugStatus.valueOf(status.toUpperCase(Locale.US));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown bug status: " + status);
        }
    }
}
