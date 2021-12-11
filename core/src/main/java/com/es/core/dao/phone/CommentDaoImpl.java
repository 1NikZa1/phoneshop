package com.es.core.dao.phone;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CommentDaoImpl implements CommentDao {
    private static final String COMMENTS_BY_PHONE_ID = "SELECT * FROM comments WHERE phoneId = ?";
    private static final String INSERT_COMMENT = "INSERT INTO comments (phoneId, username, message, date) " +
            "VALUES (:phoneId, :username, :message, :date)";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private PhoneDao phoneDao;

    @Override
    public List<Comment> findAllForPhone(Long phoneId) {
        List<Comment> comments = jdbcTemplate.query(COMMENTS_BY_PHONE_ID, new CommentsRowMapper(), phoneId);
        comments.forEach(comment -> comment.setPhone(phoneDao.get(comment.getPhone().getId()).orElseThrow(PhoneNotFoundException::new)));
        return comments;
    }

    @Override
    public void save(Comment comment) {
        SqlParameterSource namedParams = getSqlParameterSource(comment);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_COMMENT, namedParams, keyHolder);
        comment.setId(keyHolder.getKey().longValue());
    }

    private SqlParameterSource getSqlParameterSource(Comment comment) {
        return new MapSqlParameterSource()
                .addValue("phoneId", comment.getPhone().getId())
                .addValue("username", comment.getUsername())
                .addValue("message", comment.getMessage())
                .addValue("date", comment.getCreatedDate());
    }
}
