package com.es.core.service.phone;

import com.es.core.dao.phone.CommentDao;
import com.es.core.dao.phone.PhoneDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Comment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentDao commentDao;
    @Resource
    private PhoneDao phoneDao;

    @Override
    public List<Comment> getAllCommentsForPhone(Long phoneId) {
        return commentDao.findAllForPhone(phoneId);
    }

    @Override
    public void addComment(String username, String message, Long phoneId) {
        Comment comment = new Comment();
        comment.setPhone(phoneDao.get(phoneId).orElseThrow(PhoneNotFoundException::new));
        comment.setUsername(username);
        comment.setMessage(message);
        comment.setCreatedDate(LocalDateTime.now());
        commentDao.save(comment);
    }
}
