package com.codecademy.goldmedal.repositories;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoldMedalRepository extends CrudRepository<GoldMedal,Integer> {

    List<GoldMedal> findGoldMedalsByCountryOrderByYearAsc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderByYearDesc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderBySeasonAsc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderBySeasonDesc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderByCityAsc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderByCityDesc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderByNameAsc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderByNameDesc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderByEventAsc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryOrderByEventDesc(String countryName);
    List<GoldMedal> findGoldMedalsByCountryAndSeasonOrderByYearAsc(String countryName, String season);

    Integer countByCountry(String country);
    Integer countBySeason(String season);
    Integer countByGender(String gender);


}
