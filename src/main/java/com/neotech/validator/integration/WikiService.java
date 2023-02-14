package com.neotech.validator.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.neotech.validator.property.WikiProperty.CALLING_CODE_PATH;
import static com.neotech.validator.property.WikiProperty.WIKI_URL;

@Slf4j
@Service
public class WikiService {

    private final RestTemplate restTemplate;

    public WikiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<String> getHtmlListOfCountryCallingCodes() {
        try {
            return Optional.ofNullable(restTemplate.getForEntity(WIKI_URL + CALLING_CODE_PATH, String.class).getBody());
        } catch (RestClientException exception) {
            log.debug("Exception, while getting wiki html data: " + exception.getMessage());
            return Optional.empty();
        }
    }
}
