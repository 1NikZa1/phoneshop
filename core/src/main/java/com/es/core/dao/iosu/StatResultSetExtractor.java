package com.es.core.dao.iosu;

import com.es.core.model.stat.Stat;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StatResultSetExtractor implements ResultSetExtractor<List<Stat>> {
    @Override
    public List<Stat> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Stat> stats = new ArrayList<>();

        Stat stat = null;
        Map<String,String> qtyMap = null;
        while (rs.next()) {
            if (stat == null || !Objects.equals(stat.getBrand(), rs.getString("brand\\year"))) {
                stat = new Stat();
                qtyMap = new HashMap<>();
                stat.setQtyMap(qtyMap);
                stats.add(stat);
                setPhoneProps(stat, rs);
            }
            if (!rs.getString("2020").isEmpty()) {
                qtyMap.put("2020",rs.getString("2020"));
            }
            if (!rs.getString("2021").isEmpty()) {
                qtyMap.put("2021",rs.getString("2021"));
            }
            if (!rs.getString("2022").isEmpty()) {
                qtyMap.put("2022",rs.getString("2022"));
            }
        }
        return stats;
    }

    private void setPhoneProps(Stat stat, ResultSet resultSet) throws SQLException {
        stat.setBrand(resultSet.getString("brand\\year"));
    }
}
