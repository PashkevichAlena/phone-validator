package com.neotech.validator.config;

import com.neotech.validator.exception.PhoneValidatorException;
import com.neotech.validator.integration.WikiService;
import com.neotech.validator.model.dto.CountryDto;
import com.neotech.validator.service.CountryService;
import com.neotech.validator.utils.parser.HtmlParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static com.neotech.validator.exception.StatusCode.DATA_NOT_RECEIVED;

@Configuration
@Profile("!test")
public class DataLoader {
    public WikiService wikiService;
    public CountryService countryService;

    public DataLoader(WikiService wikiService, CountryService countryService) {
        this.wikiService = wikiService;
        this.countryService = countryService;
    }

    @PostConstruct
    public void load() {
        Optional<String> htmlCountriesCallingCode = wikiService.getHtmlListOfCountryCallingCodes();
        if (htmlCountriesCallingCode.isEmpty()) throw new PhoneValidatorException(DATA_NOT_RECEIVED);

        List<CountryDto> countries = HtmlParser.parse(htmlCountriesCallingCode.get());

        countryService.deleteAll();
        countryService.saveAll(countries);
    }
}
