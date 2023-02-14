package com.neotech.validator.service.impl;

import com.neotech.validator.exception.PhoneValidatorException;
import com.neotech.validator.model.Country;
import com.neotech.validator.repository.CountryRepository;
import com.neotech.validator.service.CountryService;
import com.neotech.validator.utils.validator.CallingCodeValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;

import static com.neotech.validator.exception.StatusCode.COUNTRY_NOT_FOUND;
import static com.neotech.validator.exception.StatusCode.INVALID_CALLING_CODE;
import static com.neotech.validator.service.impl.CountryServiceImpl.prepareCallingCodeForSearch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {CountryRepository.class, CountryServiceImpl.class})
class CountryServiceImplTest {

    @MockBean
    CountryRepository countryRepository;

    @Autowired
    CountryService countryService;

    private static MockedStatic<CallingCodeValidator> validatorMock;
    private static final String COUNTRY_NAME = "Belarus";
    private static final String CALLING_CODE = "+375";

    @BeforeAll
    public static void init() {
        validatorMock = mockStatic(CallingCodeValidator.class);
    }

    @AfterAll
    public static void close() {
        validatorMock.close();
    }

    @Test
    void findNameByInvalidCallingCodeMustThrowException() {
        validatorMock.when(() -> CallingCodeValidator.validate(eq(CALLING_CODE))).thenThrow(new PhoneValidatorException(INVALID_CALLING_CODE));

        PhoneValidatorException thrown = assertThrows(
                PhoneValidatorException.class,
                () -> countryService.findNameByCallingCode(CALLING_CODE)
        );
        assertEquals(INVALID_CALLING_CODE.getDefaultMessage(), thrown.getMessage());
    }

    @Test
    void findNameByUnassignedCallingCodeMustThrowException() {
        when(countryRepository.findCountriesByCallingCodeIsLike(eq(CALLING_CODE))).thenReturn(Collections.emptyList());
        PhoneValidatorException thrown = assertThrows(
                PhoneValidatorException.class,
                () -> countryService.findNameByCallingCode(CALLING_CODE)
        );
        assertEquals(COUNTRY_NOT_FOUND.getDefaultMessage(), thrown.getMessage());
    }

    @Test
    void findNameByCallingCodeIsOkay() {
        List<Country> countryResult = Collections.singletonList(new Country(COUNTRY_NAME, CALLING_CODE));
        when(countryRepository.findCountriesByCallingCodeIsLike(eq(prepareCallingCodeForSearch(CALLING_CODE)))).thenReturn(countryResult);

        List<String> result = countryService.findNameByCallingCode(CALLING_CODE);
        assertEquals(COUNTRY_NAME, result.get(0));
    }

    @Test
    void saveAllIsOkay() {
        countryService.saveAll(Collections.emptyList());
        verify(countryRepository, times(1)).saveAll(Collections.emptyList());
    }

    @Test
    void deleteAllIsOkay() {
        countryService.deleteAll();
        verify(countryRepository, times(1)).deleteAll();
    }
}