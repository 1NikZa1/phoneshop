package com.es.core.service.order;

import com.es.core.dao.order.CommentDao;
import com.es.core.model.phone.Comment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentDao commentDao;

    @Override
    public List<Comment> getAllComments() {
        return commentDao.findAll();
    }

    @Override
    public Comment getCommentForOrder(Long orderId) {
        return commentDao.getCommentForOrder(orderId);
    }

    @Override
    public void addComment(Comment comment) {
        commentDao.save(comment);
    }
}
