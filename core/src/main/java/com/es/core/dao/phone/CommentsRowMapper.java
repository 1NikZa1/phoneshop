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
        comment.setMessage(rs.getString("review"));
        if (rs.getTimestamp("reviewDate") != null)
            comment.setCreatedDate(rs.getTimestamp("reviewDate").toLocalDateTime());
        return comment;
    }
}