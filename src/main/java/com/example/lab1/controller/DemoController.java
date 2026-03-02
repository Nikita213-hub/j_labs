package com.example.lab1.controller;

import com.example.lab1.dto.BulkProjectRequest;
import com.example.lab1.service.BulkProjectService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    private final BulkProjectService bulkProjectService;

    public DemoController(BulkProjectService bulkProjectService) {
        this.bulkProjectService = bulkProjectService;
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> createBulk(
            @Valid @RequestBody BulkProjectRequest request,
            @RequestParam(defaultValue = "false") boolean transactional
    ) {
        if (transactional) {
            bulkProjectService.saveWithTransaction(request);
            return Map.of("message", "Bulk operation completed with transaction");
        }
        bulkProjectService.saveWithoutTransaction(request);
        return Map.of("message", "Bulk operation completed without transaction");
    }
}
