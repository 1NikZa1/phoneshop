package com.es.core.dao.stock;

import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockRowMapper extends BeanPropertyRowMapper<Stock> {
    @Override
    public Stock mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Stock stock = new Stock();

        Phone phone = new Phone();
        phone.setId(rs.getLong("phoneId"));

        stock.setPhone(phone);
        stock.setStock(rs.getInt("stock"));
        stock.setReserved(rs.getInt("reserved"));
        return stock;
    }
}
