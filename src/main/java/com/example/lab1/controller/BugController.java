package com.example.lab1.controller;

import com.example.lab1.dto.BugDto;
import com.example.lab1.dto.BugRequest;
import com.example.lab1.service.BugService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bugs")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @GetMapping
    public List<BugDto> getBugs(@RequestParam(required = false) String status) {
        return bugService.findBugs(status);
    }

    @GetMapping("/{id}")
    public BugDto getBug(@PathVariable UUID id) {
        return bugService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BugDto createBug(@Valid @RequestBody BugRequest request) {
        return bugService.create(request);
    }
}
