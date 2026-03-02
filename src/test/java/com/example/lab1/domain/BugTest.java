package com.example.lab1.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BugTest {

    @Test
    void shouldAttachCommentBiDirectionally() {
        Bug bug = new Bug();
        Comment comment = new Comment();

        bug.addComment(comment);

        assertThat(bug.getComments()).contains(comment);
        assertThat(comment.getBug()).isEqualTo(bug);
    }

    @Test
    void shouldAttachTagBiDirectionally() {
        Bug bug = new Bug();
        Tag tag = new Tag();
        tag.setName("backend");

        bug.addTag(tag);

        assertThat(bug.getTags()).contains(tag);
        assertThat(tag.getBugs()).contains(bug);

        bug.removeTag(tag);

        assertThat(bug.getTags()).doesNotContain(tag);
        assertThat(tag.getBugs()).doesNotContain(bug);
    }
}
