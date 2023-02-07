package com.es.core.dao.stock;

import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@RunWith(SpringRunner.class)
@Transactional
public class StockDaoTest {
    @Resource
    private StockDao stockDao;

    @Test
    public void shouldGetStockForExistingPhone() {
        Optional<Stock> stockOptional = stockDao.get(1001L);

        assertTrue(stockOptional.isPresent());
    }

    @Test
    public void shouldGetStockForNonexistentPhone() {
        Optional<Stock> stockOptional = stockDao.get(1L);

        assertFalse(stockOptional.isPresent());
    }

    @Test
    public void shouldInsertStock() {
        Phone phone = new Phone();
        phone.setId(1000L);
        Stock stock = new Stock(phone, 15, 10);

        assertFalse(stockDao.get(1000L).isPresent());

        stockDao.save(stock);

        assertTrue(stockDao.get(1000L).isPresent());
    }

    @Test
    public void shouldUpdateStock() {
        Phone phone = new Phone();
        phone.setId(1001L);
        Stock stock = new Stock(phone, 15, 10);

        assertNotEquals(stock.getStock(), stockDao.get(1001L).get().getStock());

        stockDao.save(stock);

        assertEquals(stock.getStock(), stockDao.get(1001L).get().getStock());
    }
}
