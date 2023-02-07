package com.es.core.service.order;

import com.es.core.model.phone.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAllComments();

    Comment getCommentForOrder(Long orderId);

    void addComment(Comment comment);
}
