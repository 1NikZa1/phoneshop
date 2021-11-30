package com.es.core.dao.phone;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@RunWith(SpringRunner.class)
@Transactional
public class PhoneDaoImplTest {
    private static final int dbSize = 6;
    public static final String QUERY = "apple";
    public static final String SORT_FIELD = "price";
    public static final String SORT_ORDER = "desc";

    @Resource
    private PhoneDao phoneDao;

    private Phone testPhone;

    @Before
    public void setup() {
        testPhone = new Phone();
        testPhone.setBrand("Iphone");
        testPhone.setModel("6");
        testPhone.setPrice(BigDecimal.valueOf(150));
        testPhone.setId(10L);
    }

    @Test
    public void shouldGetExistingPhone() {
        for (long id = 1000L; id <= 1004; id++) {
            Optional<Phone> phone = phoneDao.get(id);

            assertTrue(phone.isPresent());
        }
    }

    @Test
    public void shouldGetNonexistentPhone() {
        Optional<Phone> phone = phoneDao.get(0L);

        assertFalse(phone.isPresent());
    }

    @Test
    public void shouldInsertPhoneWithoutColors() {
        testPhone.setId(null);

        assertFalse(phoneDao.get(testPhone.getId()).isPresent());

        phoneDao.save(testPhone);

        assertTrue(phoneDao.get(testPhone.getId()).isPresent());
    }

    @Test
    public void shouldUpdatePhoneWithoutColors() {
        testPhone.setId(1000L);

        phoneDao.save(testPhone);

        assertEquals(phoneDao.findAll(0, 10).size(), dbSize);
    }

    @Test
    public void shouldUpdatePhoneWithColors() {
        Phone phone = phoneDao.get(1000L).get();
        Color oldColor = new Color(1000L, "Black");
        Color newColor = new Color(1001L, "White");
        phone.getColors().remove(oldColor);
        phone.getColors().add(newColor);

        phoneDao.save(phone);

        assertTrue(phoneDao.get(1000L).get().getColors().contains(newColor));
        assertFalse(phoneDao.get(1000L).get().getColors().contains(oldColor));
    }

    @Test
    public void shouldFindAll() {
        List<Phone> phones = phoneDao.findAll(0, 2);

        assertEquals(2, phones.size());
    }

    @Test
    public void shouldFindAllByQueryWithSortingByDesc() {
        List<Phone> phones = phoneDao.findAll(QUERY, SORT_FIELD, SORT_ORDER, 0, 2);

        assertEquals(1,phones.get(0).getPrice().compareTo(phones.get(1).getPrice()));
    }

    @Test
    public void shouldFindAllByQueryWithSortingByDefault() {
        List<Phone> phones = phoneDao.findAll(QUERY, SORT_FIELD, "", 0, 2);

        assertEquals(-1,phones.get(0).getPrice().compareTo(phones.get(1).getPrice()));
    }

    @Test
    public void shouldFindAllByEmptyQueryWithSortingByDefault() {
        List<Phone> phones = phoneDao.findAll("", SORT_FIELD, "", 0, 10);

        assertEquals(dbSize,phones.size());
        assertEquals(-1,phones.get(0).getPrice().compareTo(phones.get(phones.size()-1).getPrice()));
    }

    @Test
    public void shouldFindAllByEmptyQueryWithoutSorting() {
        List<Phone> phones = phoneDao.findAll("", SORT_FIELD, "", 0, 10);

        assertEquals(dbSize,phones.size());
        assertEquals(-1,phones.get(0).getPrice().compareTo(phones.get(phones.size()-1).getPrice()));
    }
}
