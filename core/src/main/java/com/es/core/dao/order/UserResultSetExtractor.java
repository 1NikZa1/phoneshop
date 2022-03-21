package com.es.core.dao.order;

import com.es.core.model.order.Order;
import com.es.core.model.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();

        User user = null;
        while (rs.next()) {
            if (user == null || user.getId() != rs.getLong("id")) {
                user = new User();
                users.add(user);
                setPhoneProps(user, rs);
            }
            Order order = new Order();

        }
        return users;
    }

    private void setPhoneProps(User user, ResultSet resultSet) throws SQLException {
        user.setId(resultSet.getLong("id"));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setContactPhoneNo(resultSet.getString("contactPhoneNo"));
    }
}
