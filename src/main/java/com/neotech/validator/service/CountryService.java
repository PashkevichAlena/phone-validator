package com.neotech.validator.service;

import com.neotech.validator.model.dto.CountryDto;

import java.util.List;


public interface CountryService {
    List<String> findNameByCallingCode(String callingCode);

    void deleteAll();

    void saveAll(List<CountryDto> countryDtoList);
}
