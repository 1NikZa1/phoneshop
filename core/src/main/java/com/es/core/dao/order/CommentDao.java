package com.es.core.dao.order;

import com.es.core.model.phone.Comment;

import java.util.List;

public interface CommentDao {

    List<Comment> findAll();

    Comment getCommentForOrder(Long orderId);

    void save(Comment comment);

}
