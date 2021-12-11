package com.es.core.dao.phone;

import com.es.core.model.phone.Comment;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentsRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        Phone phone = new Phone();
        phone.setId(rs.getLong("phoneId"));
        comment.setMessage(rs.getString("message"));
        comment.setUsername(rs.getString("username"));
        comment.setCreatedDate(rs.getTimestamp("date").toLocalDateTime());
        comment.setPhone(phone);
        return comment;
    }
}