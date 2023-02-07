package com.es.core.service.phone;

import com.es.core.model.phone.Brand;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface PhoneService {
    Optional<Phone> getPhone(Long productId);

    Optional<Brand> getBrand(Long brandId);

    Optional<Color> getColor(Long colorId);

    void savePhone(Phone phone);

    void saveBrand(Brand brand);

    void saveColor(Color color);

    void delete(Phone phone);

    void deleteBrand(Brand brand);

    void deleteColor(Color color);

    Set<Color> getColors();

    Map<String,String> getAllBrands();

    Map<String,String> getAllDeviceTypes();

    Map<String,String> getAllOpSystems();

    Optional<Phone> getPhoneByModel(String model);

    List<Phone> findPhones(String query, String sortField, String sortOrder, int pageNumber, int phonesPerPage);

    int countPhones(String query);
}
