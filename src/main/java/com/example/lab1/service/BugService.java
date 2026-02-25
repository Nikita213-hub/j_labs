package com.example.lab1.service;

import com.example.lab1.domain.Bug;
import com.example.lab1.domain.BugStatus;
import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.BugRequest;
import com.example.lab1.exception.BugNotFoundException;
import com.example.lab1.mapper.BugMapper;
import com.example.lab1.repository.BugRepository;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BugService {

    private final BugRepository bugRepository;
    private final BugMapper bugMapper;

    public BugService(BugRepository bugRepository, BugMapper bugMapper) {
        this.bugRepository = bugRepository;
        this.bugMapper = bugMapper;
    }

    public List<BugDto> findBugs(String statusFilter) {
        List<Bug> bugs = StringUtils.hasText(statusFilter)
                ? bugRepository.findByStatus(resolveStatus(statusFilter))
                : bugRepository.findAll();
        return bugMapper.toDtoList(bugs);
    }

    public BugDto findById(UUID id) {
        Bug bug = bugRepository.findById(id).orElseThrow(() -> new BugNotFoundException(id));
        return bugMapper.toDto(bug);
    }

    public BugDto create(BugRequest request) {
        Bug toSave = bugMapper.toEntity(request);
        Bug saved = bugRepository.save(toSave);
        return bugMapper.toDto(saved);
    }

    private BugStatus resolveStatus(String status) {
        try {
            return BugStatus.valueOf(status.toUpperCase(Locale.US));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown bug status: " + status);
        }
    }
}
