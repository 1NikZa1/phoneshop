package com.es.core.dao.order;

import com.es.core.model.phone.Comment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentResultSetExtractor implements ResultSetExtractor<Comment> {
    @Override
    public Comment extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Comment comment = new Comment();
        while (resultSet.next()) {
            comment.setMessage(resultSet.getString("review"));
            if (comment.getMessage() != null || !comment.getMessage().isEmpty()) {
                comment.setCreatedDate(resultSet.getTimestamp("reviewDate").toLocalDateTime());
            }
        }
        return comment;
    }
}
