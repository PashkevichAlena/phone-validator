package com.neotech.validator.repository;

import com.neotech.validator.ValidatorApplication;
import com.neotech.validator.model.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.neotech.validator.service.impl.CountryServiceImpl.prepareCallingCodeForSearch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(classes = ValidatorApplication.class)
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    private static final Country BELARUS = new Country("Belarus", "[\"+1\"]");
    private static final Country USA = new Country("USA", "[\"+123\",\"+12\"]");
    private static final String BELARUS_CALLING_CODE = "+1";
    private static final String UNASSIGNED_CALLING_CODE = "+9";

    @Test
    public void findCountryByCallingCodeIsOkay() {
        countryRepository.save(BELARUS);
        countryRepository.save(USA);
        List<Country> countries = countryRepository.findCountriesByCallingCodeIsLike(prepareCallingCodeForSearch(BELARUS_CALLING_CODE));

        assertFalse(countries.isEmpty());
        assertEquals(BELARUS.getName(), countries.get(0).getName());
    }

    @Test
    public void findCountryByUnassignedCallingCodeIsOkay() {
        countryRepository.save(BELARUS);
        countryRepository.save(USA);
        List<Country> country = countryRepository.findCountriesByCallingCodeIsLike(prepareCallingCodeForSearch(UNASSIGNED_CALLING_CODE));

        assertTrue(country.isEmpty());
    }
}