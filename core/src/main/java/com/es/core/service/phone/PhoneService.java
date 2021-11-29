package com.es.core.service.phone;

import com.es.core.model.phone.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneService {
    Optional<Phone> getPhone(Long productId);

    List<Phone> findPhones(String query, String sortField, String sortOrder, int pageNumber, int phonesPerPage);

    int countPhones(String query);
}
