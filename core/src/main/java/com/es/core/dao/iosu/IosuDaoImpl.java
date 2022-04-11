package com.es.core.dao.iosu;

import com.es.core.model.stat.Stat;
import com.es.core.model.stat.UnionStat;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class IosuDaoImpl implements IosuDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public static final String GET_STAT = "SELECT brands.name AS `brand\\year`, " +
            "SUM(CASE WHEN  orders.date > '2020-01-01 00:00:00' and orders.date < '2020-12-31 23:59:59' THEN order_items.quantity ELSE 0 END) AS \"2020\",\n" +
            "SUM(CASE WHEN  orders.date > '2021-01-01 00:00:00' and orders.date < '2021-12-31 23:59:59' THEN order_items.quantity ELSE 0 END) AS \"2021\",\n" +
            "SUM(CASE WHEN  orders.date > '2022-01-01 00:00:00' and orders.date < '2022-12-31 23:59:59' THEN order_items.quantity ELSE 0 END) AS \"2022\"\n" +
            "FROM order_items " +
            "LEFT JOIN phones ON phones.id = order_items.phone " +
            "LEFT JOIN orders ON orders.id = order_items.`order` " +
            "LEFT JOIN brands ON brands.id = phones.brand " +
            "GROUP BY brands.name " +
            "ORDER BY brands.name";
    public static final String GET_UNION_STAT = "SELECT brands.name, GROUP_CONCAT(DISTINCT phones.model SEPARATOR ', ') AS phones " +
            "FROM brands " +
            "LEFT JOIN phones ON phones.brand = brands.id " +
            "WHERE brands.id = ? " +
            "GROUP BY brands.name " +
            "union all " +
            "SELECT device_types.name, GROUP_CONCAT(DISTINCT phones.model SEPARATOR ', ') as phones " +
            "FROM device_types " +
            "LEFT JOIN phones ON phones.deviceType = device_types.id " +
            "WHERE device_types.id = ? " +
            "GROUP BY device_types.name";

    @Override
    public void createTableForDate(LocalDateTime date) {
        Timestamp timestamp1 = Timestamp.valueOf(date.toLocalDate().atStartOfDay());
        Timestamp timestamp2 = Timestamp.valueOf(date.toLocalDate().atTime(LocalTime.MAX));
        jdbcTemplate.execute("CREATE TABLE orders_for_" + date.toLocalDate() + "AS SELECT id, secureId, totalPrice " +
                "WHERE date between " + timestamp1 + " AND " + timestamp2);
    }

    @Override
    public List<Stat> getStat() {
        return jdbcTemplate.query(GET_STAT, new StatResultSetExtractor());
    }

    @Override
    public List<UnionStat> getUnionStat(Long brandId, Long typeId) {
        return jdbcTemplate.query(GET_UNION_STAT, new UnionStatResultSetExtractor(),brandId,typeId);
    }
}
