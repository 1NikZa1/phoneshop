package com.es.core.dao.phone;

import com.es.core.model.phone.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentsRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setMessage(rs.getString("review"));
        comment.setUser(rs.getLong("user"));
        if (rs.getTimestamp("reviewDate") != null)
            comment.setCreatedDate(rs.getTimestamp("reviewDate").toLocalDateTime());
        return comment;
    }
}