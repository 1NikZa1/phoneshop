package com.es.core.service.stock;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.dao.stock.StockDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.stock.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceUnitTest {
    public static final Long EXISTING_PHONE_ID = 1000L;
    public static final Long NONEXISTING_PHONE_ID = 1L;
    @Mock
    private StockDao stockDao;
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private Stock stock;
    @Mock
    private Phone phone;

    private StockService stockService;

    @Before
    public void setup() {
        stockService = new StockServiceImpl(stockDao, phoneDao);

        when(stock.getPhone()).thenReturn(phone);
        when(phone.getId()).thenReturn(EXISTING_PHONE_ID);
        when(stockDao.get(EXISTING_PHONE_ID)).thenReturn(Optional.of(stock));
        when(stockDao.get(NONEXISTING_PHONE_ID)).thenReturn(Optional.empty());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetStockForExistingPhoneWithoutId() {
        stockService.getStock(EXISTING_PHONE_ID);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldGetStockForExistingPhoneWithId() {
        when(phone.getId()).thenReturn(EXISTING_PHONE_ID);
        stockService.getStock(EXISTING_PHONE_ID);

        verify(stockDao).get(EXISTING_PHONE_ID);
    }

    @Test
    public void shouldGetStockForNonexistingPhone() {
        stockService.getStock(NONEXISTING_PHONE_ID);

        verify(stockDao,times(1)).get(NONEXISTING_PHONE_ID);
    }

    @Test
    public void shouldSaveStock(){
        stockService.save(stock);

        verify(stockDao,times(1)).save(stock);
    }
}