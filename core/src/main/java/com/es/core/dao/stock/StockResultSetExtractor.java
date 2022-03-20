package com.es.core.dao.stock;

import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockResultSetExtractor implements ResultSetExtractor<Stock> {
    @Override
    public Stock extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Stock stock = new Stock();

        Phone phone = new Phone();
        while (resultSet.next()) {
            phone.setId(resultSet.getLong("id"));
            stock.setPhone(phone);
            stock.setStock(resultSet.getInt("stock"));
            stock.setReserved(resultSet.getInt("reserved"));
        }
        return stock;
    }
}
