package com.es.core.service.stock;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {
    private StockDao stockDao;
    private PhoneDao phoneDao;

    public StockServiceImpl(StockDao stockDao, PhoneDao phoneDao) {
        this.stockDao = stockDao;
        this.phoneDao = phoneDao;
    }

    @Override
    public Optional<Stock> getStock(Long phoneId) {
        Optional<Stock> stockOptional = stockDao.get(phoneId);
        if (stockOptional.isPresent()) {
            Phone phone = phoneDao.get(stockOptional.get().getPhone().getId())
                    .orElseThrow(NoSuchElementException::new);
            stockOptional.get().setPhone(phone);
        }
        return stockOptional;
    }

    @Override
    public void save(Stock stock) {
        stockDao.save(stock);
    }
}
