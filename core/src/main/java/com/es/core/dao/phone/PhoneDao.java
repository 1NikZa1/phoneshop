package com.es.core.dao.phone;

import com.es.core.model.phone.Brand;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    Optional<Brand> getBrand(Long key);

    Optional<Color> getColor(final Long key);

    Optional<Phone> getByModel(String model);

    Set<Color> getAllColors();

    Map<String,String> getAllBrands();

    Map<String,String> getAllDeviceTypes();

    Map<String,String> getAllOpSystems();

    void save(Phone phone);

    void delete(Phone phone);

    void saveBrand(Brand brand);

    void saveColor(Color color);

    void deleteBrand(Brand brand);

    void deleteColor(Color color);

    List<Phone> findAll(int offset, int limit);

    List<Phone> findAll(String query, String sortField, String sortOrder, int offset, int limit);

    int countPhones(String query);
}
