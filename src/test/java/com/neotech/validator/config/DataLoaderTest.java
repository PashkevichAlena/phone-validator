package com.neotech.validator.config;

import com.neotech.validator.exception.PhoneValidatorException;
import com.neotech.validator.integration.WikiService;
import com.neotech.validator.service.CountryService;
import com.neotech.validator.utils.parser.HtmlParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.Optional;

import static com.neotech.validator.exception.StatusCode.DATA_NOT_RECEIVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {WikiService.class})
class DataLoaderTest {

    @MockBean
    public WikiService wikiService;

    @MockBean
    public CountryService countryService;

    private static MockedStatic<HtmlParser> parserMock;

    @BeforeAll
    public static void init() {
        parserMock = mockStatic(HtmlParser.class);
    }

    @AfterAll
    public static void close() {
        parserMock.close();
    }

    @Test
    void loadDataOnNotReceivedHtmlMustThrowException() {
        when(wikiService.getHtmlListOfCountryCallingCodes()).thenReturn(Optional.empty());

        DataLoader dataLoader = new DataLoader(wikiService, countryService);
        PhoneValidatorException thrown = assertThrows(
                PhoneValidatorException.class,
                dataLoader::load
        );

        assertEquals(DATA_NOT_RECEIVED.getDefaultMessage(), thrown.getMessage());
    }

    @Test
    void loadDataIsOkay() {
        String htmlValue = "htmlValue";

        when(wikiService.getHtmlListOfCountryCallingCodes()).thenReturn(Optional.of(htmlValue));
        parserMock.when(() -> HtmlParser.parse(eq(htmlValue))).thenReturn(Collections.emptyList());

        DataLoader dataLoader = new DataLoader(wikiService, countryService);
        dataLoader.load();

        verify(countryService, times(1)).deleteAll();
        verify(countryService, times(1)).saveAll(Collections.emptyList());
    }
}