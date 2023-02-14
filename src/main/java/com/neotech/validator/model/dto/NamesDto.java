package com.neotech.validator.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NamesDto {
    private List<String> countryNames;
}