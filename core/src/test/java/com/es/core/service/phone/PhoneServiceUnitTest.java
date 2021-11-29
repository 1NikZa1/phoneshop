package com.es.core.service.phone;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhoneServiceUnitTest {
    public static final String PHONE_BRAND_1 = "apple";
    public static final String PHONE_BRAND_2 = "samsung";
    @Mock
    private Phone phone1;
    @Mock
    private Phone phone2;
    @Mock
    private PhoneDao phoneDao;
    @Spy
    private ArrayList<Phone> phones;

    private PhoneService phoneService;

    @Before
    public void setup() {
        phoneService = new PhoneServiceImpl(phoneDao);

        phones.add(phone1);
        phones.add(phone2);

        when(phone1.getBrand()).thenReturn(PHONE_BRAND_1);
        when(phone2.getBrand()).thenReturn(PHONE_BRAND_2);

        when(phoneDao.findAll(any(), any(), any(), anyInt(), anyInt())).thenReturn(phones);
    }

    @Test
    public void shouldFindPhonesByQueryAndSortField() {
        phoneService.findPhones("iphone", "brand", null, 1, 10);

        verify(phone1).getBrand();
        verify(phone2).getBrand();
    }

    @Test
    public void shouldFindPhonesByQueryWithoutSortField() {
        phoneService.findPhones("iphone", null, null, 1, 10);

        verify(phones, never()).sort(any());
    }

    @Test
    public void shouldCountPhones() {
        phoneService.countPhones("iphone");

        verify(phoneDao).countPhones("iphone");
    }
}
