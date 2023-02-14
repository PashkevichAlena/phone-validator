package com.neotech.validator.service.impl;

import com.google.gson.Gson;
import com.neotech.validator.exception.PhoneValidatorException;
import com.neotech.validator.model.Country;
import com.neotech.validator.model.dto.CountryDto;
import com.neotech.validator.repository.CountryRepository;
import com.neotech.validator.service.CountryService;
import com.neotech.validator.utils.validator.CallingCodeValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.neotech.validator.exception.StatusCode.COUNTRY_NOT_FOUND;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {

        this.countryRepository = countryRepository;
    }

    public List<String> findNameByCallingCode(String callingCode) {
        CallingCodeValidator.validate(callingCode);
        List<Country> country = countryRepository.findCountriesByCallingCodeIsLike(prepareCallingCodeForSearch(callingCode));
        if (CollectionUtils.isEmpty(country)) throw new PhoneValidatorException(COUNTRY_NOT_FOUND);
        return country.stream().map(Country::getName).collect(Collectors.toList());
    }

    public void saveAll(List<CountryDto> countryDtoList) {
        List<Country> countries = countryDtoList.stream()
                .map(this::buildCountry)
                .collect(Collectors.toList());

        countryRepository.saveAll(countries);
    }

    public void deleteAll() {
        countryRepository.deleteAll();
    }

    public static String prepareCallingCodeForSearch(String callingCode) {
        return String.format("%%\"%s\"%%", callingCode);
    }

    private Country buildCountry(CountryDto countryDto) {
        return new Country(countryDto.getName(), new Gson().toJson(countryDto.getCallingCodes()));
    }
}
