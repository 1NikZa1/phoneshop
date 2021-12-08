package com.es.core.dao.phone;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@RunWith(SpringRunner.class)
public class PhoneResultSetExtractorTest {
    private static final String PHONE_BY_ID = "SELECT phones.*, colors.id AS colorId, colors.code AS colorCode " +
            "FROM phones " +
            "LEFT JOIN phone2color ON phone2color.phoneId = phones.id " +
            "LEFT JOIN colors ON colors.id = phone2color.colorId " +
            "WHERE phones.id = ?";

    @Resource
    private JdbcTemplate jdbcTemplate;

    private Phone phone;
    private PhoneResultSetExtractor phoneResultSetExtractor;

    @Before
    public void setup() {
        phoneResultSetExtractor = new PhoneResultSetExtractor();
    }

    @Test
    public void shouldExtractDataWithColors() {
        phone = jdbcTemplate.query(PHONE_BY_ID, phoneResultSetExtractor, 1000L).get(0);
        Color color = new Color(1000L, "Black");

        assertEquals("ARCHOS 101 G9", phone.getModel());
        assertTrue(phone.getColors().contains(color));
    }

    @Test
    public void shouldExtractDataWithoutColors() {
        phone = jdbcTemplate.query(PHONE_BY_ID, phoneResultSetExtractor, 1004L).get(0);

        assertEquals(0, phone.getColors().size());
    }
}
