package com.es.core.dao.order;

import com.es.core.dao.phone.CommentsRowMapper;
import com.es.core.dao.stock.StockResultSetExtractor;
import com.es.core.model.phone.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CommentDaoImpl implements CommentDao {
    private static final String ALL_COMMENTS = "SELECT review, reviewDate, id FROM orders";
    private static final String COMMENT_FOR_ORDER = "SELECT review, reviewDate FROM orders WHERE id = ?";
    private static final String INSERT_COMMENT = "UPDATE orders SET review = :review, reviewDate = :reviewDate " +
            "WHERE id = :id ";
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Comment> findAll() {
        return jdbcTemplate.query(ALL_COMMENTS, new CommentsRowMapper());
    }

    @Override
    public Comment getCommentForOrder(Long orderId) {
        Comment comment = jdbcTemplate.query(COMMENT_FOR_ORDER, new CommentsRowMapper(), orderId).stream().findFirst().get();
        if (comment != null) {
            comment.setId(orderId);
        }
        return comment;
    }

    @Override
    public void save(Comment comment) {
        SqlParameterSource namedParams = getSqlParameterSource(comment);
        namedParameterJdbcTemplate.update(INSERT_COMMENT, namedParams);
    }

    private SqlParameterSource getSqlParameterSource(Comment comment) {
        return new MapSqlParameterSource()
                .addValue("id", comment.getId())
                .addValue("review", comment.getMessage())
                .addValue("reviewDate", comment.getCreatedDate());
    }
}
