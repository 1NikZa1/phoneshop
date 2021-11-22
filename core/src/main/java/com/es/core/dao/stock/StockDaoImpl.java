package com.es.core.dao.stock;

import com.es.core.model.stock.Stock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class StockDaoImpl implements StockDao {
    private static final String STOCK_BY_PHONE_ID = "SELECT * FROM stocks " +
            "WHERE phoneId = ?";
    private static final String INSERT_STOCK = "INSERT INTO stocks (phoneId, stock, reserved) " +
            "VALUES (:phoneId, :stock, :reserved)";
    private static final String UPDATE_STOCK = "UPDATE stocks SET stock = :stock, reserved = :reserved " +
            "WHERE phoneId = :phoneId";

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Stock> get(Long phoneId) {
        Stock stock = jdbcTemplate.query(STOCK_BY_PHONE_ID, new StockResultSetExtractor(), phoneId);
        if (stock.getPhone() == null) {
            return Optional.empty();
        }
        return Optional.of(stock);
    }

    @Override
    public void save(Stock stock) {
        Optional<Stock> stockOptional = get(stock.getPhone().getId());
        if (!stockOptional.isPresent()) {
            insert(stock);
        } else {
            update(stock);
        }
    }

    private void insert(Stock stock) {
        SqlParameterSource namedParams = getSqlParameterSource(stock);
        namedParameterJdbcTemplate.update(INSERT_STOCK, namedParams);
    }

    private void update(Stock stock) {
        SqlParameterSource namedParams = getSqlParameterSource(stock);
        namedParameterJdbcTemplate.update(UPDATE_STOCK, namedParams);
    }

    private SqlParameterSource getSqlParameterSource(Stock stock) {
        return new MapSqlParameterSource()
                .addValue("phoneId", stock.getPhone().getId())
                .addValue("stock", stock.getStock())
                .addValue("reserved", stock.getReserved());
    }


}
