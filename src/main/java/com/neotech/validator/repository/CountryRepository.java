package com.neotech.validator.repository;

import com.neotech.validator.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    List<Country> findCountriesByCallingCodeIsLike(String callingCode);
}
