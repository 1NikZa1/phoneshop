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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@RunWith(SpringRunner.class)
@Transactional
public class JdbcPhoneDaoTest {
    @Resource
    private PhoneDao phoneDao;

    private Phone testPhone;
    private static final int dbSize = 5;

    @Before
    public void setup() {
        testPhone = new Phone();
        testPhone.setBrand("Iphone");
        testPhone.setModel("6");
    }

    @Test
    public void shouldGetExistingPhone() {
        for (long id = 1000L; id <= 1004; id++) {
            Optional<Phone> phone = phoneDao.get(id);

            assertTrue(phone.isPresent());
        }
    }

    @Test
    public void shouldGetNonexistingPhone() {
        Optional<Phone> phone = phoneDao.get(0L);

        assertFalse(phone.isPresent());
    }

    @Test
    public void shouldInsertPhoneWithoutColors() {
        phoneDao.save(testPhone);

        assertEquals(phoneDao.findAll(0, 10).size(), dbSize + 1);
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
        Color oldColor = new Color(1000L,"Black");
        Color newColor = new Color(1001L,"White");
        phone.getColors().remove(oldColor);
        phone.getColors().add(newColor);

        phoneDao.save(phone);

        assertTrue(phoneDao.get(1000L).get().getColors().contains(newColor));
        assertFalse(phoneDao.get(1000L).get().getColors().contains(oldColor));
    }

    @Test
    public void shouldFindAll() {
        List<Phone> phones = phoneDao.findAll(0, 2);

        for (Phone phone : phones) {
            assertFalse(phone.getColors().isEmpty());
        }
    }
}
