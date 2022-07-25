package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CountriesRepository extends CrudRepository<Country,Integer> {
    Optional<Country> getByName(String country);

    List<Country> getCountriesByOrderByNameAsc();
    List<Country> getCountriesByOrderByNameDesc();
    List<Country> getCountriesByOrderByCodeAsc();
    List<Country> getCountriesByOrderByCodeDesc();
    List<Country> getCountriesByOrderByPopulationAsc();
    List<Country> getCountriesByOrderByPopulationDesc();
    List<Country> getCountriesByOrderByGdpAsc();
    List<Country> getCountriesByOrderByGdpDesc();



}
