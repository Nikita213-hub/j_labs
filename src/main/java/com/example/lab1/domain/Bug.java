package com.example.lab1.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Bug {

    private UUID id;
    private String title;
    private String description;
    private BugStatus status;
    private BugPriority priority;
    private String reporter;
    private String assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Bug() {
    }

    private Bug(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.status = builder.status;
        this.priority = builder.priority;
        this.reporter = builder.reporter;
        this.assignee = builder.assignee;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BugStatus getStatus() {
        return status;
    }

    public void setStatus(BugStatus status) {
        this.status = status;
    }

    public BugPriority getPriority() {
        return priority;
    }

    public void setPriority(BugPriority priority) {
        this.priority = priority;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bug bug = (Bug) o;
        return Objects.equals(id, bug.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Builder {
        private UUID id;
        private String title;
        private String description;
        private BugStatus status;
        private BugPriority priority;
        private String reporter;
        private String assignee;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(BugStatus status) {
            this.status = status;
            return this;
        }

        public Builder priority(BugPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder reporter(String reporter) {
            this.reporter = reporter;
            return this;
        }

        public Builder assignee(String assignee) {
            this.assignee = assignee;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Bug build() {
            return new Bug(this);
        }
    }
}
