package com.es.core.dao.phone;

import com.es.core.model.phone.Comment;


import java.util.List;

public interface CommentDao {

    List<Comment> findAllForPhone(Long phoneId);

    void save(Comment comment);

}
