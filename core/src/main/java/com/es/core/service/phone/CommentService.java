package com.es.core.service.phone;

import com.es.core.model.phone.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAllCommentsForPhone(Long phoneId);

    void addComment(String username, String message, Long phoneId);
}
