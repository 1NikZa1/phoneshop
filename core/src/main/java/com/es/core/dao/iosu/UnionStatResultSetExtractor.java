package com.es.core.dao.iosu;

import com.es.core.model.stat.UnionStat;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UnionStatResultSetExtractor implements ResultSetExtractor<List<UnionStat>> {
    @Override
    public List<UnionStat> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<UnionStat> stats = new ArrayList<>();

        UnionStat stat = null;
        while (rs.next()) {
            if (stat == null || !Objects.equals(stat.getName(), rs.getString("name"))) {
                stat = new UnionStat();
                stat.setName(rs.getString("name"));
                stat.setPhoneModels(rs.getString("phones"));
                stats.add(stat);
            }
        }
        return stats;
    }
}
