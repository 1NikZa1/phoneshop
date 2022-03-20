package com.es.core.service.phone;

import com.es.core.dao.phone.PhoneDao;
import com.es.core.model.phone.Brand;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class PhoneServiceImpl implements PhoneService {

    private PhoneDao phoneDao;

    public PhoneServiceImpl(PhoneDao phoneDao) {
        this.phoneDao = phoneDao;
    }

    @Override
    public Optional<Phone> getPhone(Long productId) {
        return phoneDao.get(productId);
    }

    @Override
    public Optional<Brand> getBrand(Long brandId) {
        return phoneDao.getBrand(brandId);
    }

    @Override
    public Optional<Color> getColor(Long colorId) {
        return phoneDao.getColor(colorId);
    }

    @Override
    public void savePhone(Phone phone) {
        phoneDao.save(phone);
    }

    @Override
    public void saveBrand(Brand brand) {
        phoneDao.saveBrand(brand);
    }

    @Override
    public void saveColor(Color color) {
        phoneDao.saveColor(color);
    }

    @Override
    public void delete(Phone phone) {
        phoneDao.delete(phone);
    }

    @Override
    public void deleteBrand(Brand brand) {
        phoneDao.deleteBrand(brand);
    }

    @Override
    public void deleteColor(Color color) {
        phoneDao.deleteColor(color);
    }

    @Override
    public Set<Color> getColors() {
        return phoneDao.getAllColors();
    }

    @Override
    public Map<String, String> getAllBrands() {
        return phoneDao.getAllBrands();
    }

    @Override
    public Map<String, String> getAllDeviceTypes() {
        return phoneDao.getAllDeviceTypes();
    }

    @Override
    public Map<String, String> getAllOpSystems() {
        return phoneDao.getAllOpSystems();
    }

    @Override
    public Optional<Phone> getPhoneByModel(String model) {
        return phoneDao.getByModel(model);
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
