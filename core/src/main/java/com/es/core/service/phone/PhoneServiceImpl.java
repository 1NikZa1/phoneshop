package com.es.core.service.phone;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.phone.Phone;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PhoneServiceImpl implements PhoneService {

    private PhoneDao phoneDao;

    public PhoneServiceImpl(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    public List<Phone> findPhones(String query, String sortField, String sortOrder, int pageNumber, int phonesPerPage) {
        List<Phone> phones = phoneDao.findAll(query, sortField, sortOrder, (pageNumber - 1) * phonesPerPage, phonesPerPage);

        return sortPhones(sortField, sortOrder, phones);
    }

    @Override
    public int countPhones(String query) {
        return phoneDao.countPhones(query);
    }

    private List<Phone> sortPhones(String sortField, String sortOrder, List<Phone> phones) {
        if (sortField == null) {
            return phones;
        }

        Comparator<Phone> comparator = getComparatorForSortField(sortField);

        if ("desc".equals(sortOrder)) {
            comparator = comparator.reversed();
        }

        phones.sort(comparator);
        return phones;
    }

    private Comparator<Phone> getComparatorForSortField(String sortField) {
        switch (sortField) {
            case "brand":
                return Comparator.comparing(phone -> phone.getBrand().toLowerCase());
            case "price":
                return Comparator.comparing(Phone::getPrice);
            case "model":
                return Comparator.comparing(phone -> phone.getModel().toLowerCase());
            case "displaySize":
                return Comparator.comparing(Phone::getDisplaySizeInches);
            default:
                return Comparator.comparing(Phone::getId);
        }

    }
}
