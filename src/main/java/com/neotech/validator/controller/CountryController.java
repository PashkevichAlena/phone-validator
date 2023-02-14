package com.neotech.validator.controller;

import com.neotech.validator.model.dto.NamesDto;
import com.neotech.validator.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/by/{callingCode}")
    public NamesDto findNameByCallingCode(@PathVariable String callingCode) {
        List<String> countryNames = countryService.findNameByCallingCode(callingCode);
        return new NamesDto(countryNames);
    }
}
