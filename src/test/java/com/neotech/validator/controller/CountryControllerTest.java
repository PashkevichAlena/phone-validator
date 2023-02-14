package com.neotech.validator.controller;

import com.google.gson.Gson;
import com.neotech.validator.exception.GlobalExceptionHandler;
import com.neotech.validator.exception.PhoneValidatorException;
import com.neotech.validator.model.dto.NamesDto;
import com.neotech.validator.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.neotech.validator.exception.StatusCode.COUNTRY_NOT_FOUND;
import static com.neotech.validator.exception.StatusCode.INVALID_CALLING_CODE;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {CountryController.class})
class CountryControllerTest {

    private MockMvc mockMvc;

    @MockBean
    CountryService countryService;

    @Autowired
    CountryController equipmentsController;

    private static final String COUNTRY_NAME = "Belarus";
    private static final String CALLING_CODE = "+375";
    private static final String COUNTRY_URL = "/countries/by/";

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(equipmentsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testFindNameByCallingCodeIsOkay() throws Exception {
        when(countryService.findNameByCallingCode(eq(CALLING_CODE))).thenReturn(singletonList(COUNTRY_NAME));

        MvcResult mvcResult = mockMvc.perform(get(COUNTRY_URL + CALLING_CODE).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertEquals(new Gson().toJson(new NamesDto(singletonList(COUNTRY_NAME))), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testFindNameByInvalidCallingMustThrowException() throws Exception {
        when(countryService.findNameByCallingCode(eq(CALLING_CODE))).thenThrow(new PhoneValidatorException(INVALID_CALLING_CODE));

        MvcResult mvcResult = mockMvc.perform(get(COUNTRY_URL + CALLING_CODE).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andDo(print())
                .andReturn();

        assertEquals(INVALID_CALLING_CODE.getDefaultMessage(), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testFindNameByUnassignedCallingMustThrowException() throws Exception {
        when(countryService.findNameByCallingCode(eq(CALLING_CODE))).thenThrow(new PhoneValidatorException(COUNTRY_NOT_FOUND));

        MvcResult mvcResult = mockMvc.perform(get(COUNTRY_URL + CALLING_CODE).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andDo(print())
                .andReturn();

        assertEquals(COUNTRY_NOT_FOUND.getDefaultMessage(), mvcResult.getResponse().getContentAsString());
    }
}