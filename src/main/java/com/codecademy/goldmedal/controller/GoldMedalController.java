package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repositories.CountriesRepository;
import com.codecademy.goldmedal.repositories.GoldMedalRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    // TODO: declare references to your repositories
    private final CountriesRepository countriesRepository;
    private final GoldMedalRepository goldMedalRepository;

    // TODO: update your constructor to include your repositories
    @Autowired
    public GoldMedalController(CountriesRepository countriesRepository, final GoldMedalRepository goldMedalRepository) {
        this.countriesRepository = countriesRepository;
        this.goldMedalRepository = goldMedalRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                if (ascendingOrder){
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderByYearAsc(countryName);// TODO: list of medals sorted by year in the given order
                } else
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderByYearDesc(countryName);
                break;
            case "season":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderBySeasonAsc(countryName);// TODO: list of medals sorted by season in the given order
                } else
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderBySeasonDesc(countryName);
                break;
            case "city":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderByCityAsc(countryName);// TODO: list of medals sorted by city in the given order
                } else
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderByCityDesc(countryName);
                break;
            case "name":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderByNameAsc(countryName);// TODO: list of medals sorted by athlete's name in the given order
                } else
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderByNameDesc(countryName);
                break;
            case "event":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findGoldMedalsByCountryOrderByEventAsc(countryName);// TODO: list of medals sorted by athlete's name in the given order
                } else
                    medalsList =this.goldMedalRepository.findGoldMedalsByCountryOrderByEventDesc(countryName);
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        Optional<Country> countryOptional = this.countriesRepository.getByName(countryName);// TODO: get the country; this repository method should return a java.util.Optional
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        var goldMedalCount = this.goldMedalRepository.countByCountry(countryName);// TODO: get the medal count

        var summerWins = this.goldMedalRepository.findGoldMedalsByCountryAndSeasonOrderByYearAsc(countryName,"Summer");// TODO: get the collection of wins at the Summer Olympics, sorted by year in ascending order
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
        var totalSummerEvents = this.goldMedalRepository.countBySeason("Summer");// TODO: get the total number of events at the Summer Olympics
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        var winterWins = this.goldMedalRepository.findGoldMedalsByCountryAndSeasonOrderByYearAsc(countryName, "Winter");// TODO: get the collection of wins at the Winter Olympics
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        var totalWinterEvents = this.goldMedalRepository.countBySeason("Winter");// TODO: get the total number of events at the Winter Olympics, sorted by year in ascending order
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        var numberEventsWonByFemaleAthletes = this.goldMedalRepository.countByGender("Female");// TODO: get the number of wins by female athletes
        var numberEventsWonByMaleAthletes = this.goldMedalRepository.countByGender("Male");// TODO: get the number of wins by male athletes

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                if (ascendingOrder) {
                    countries = this.countriesRepository.getCountriesByOrderByNameAsc();// TODO: list of countries sorted by name in the given order
                } else
                    countries = this.countriesRepository.getCountriesByOrderByNameDesc();
                break;
            case "gdp":
                if (ascendingOrder){
                    countries = this.countriesRepository.getCountriesByOrderByGdpAsc();// TODO: list of countries sorted by gdp in the given order
                } else
                    countries = this.countriesRepository.getCountriesByOrderByGdpDesc();
                break;
            case "population":
                if (ascendingOrder) {
                    countries = this.countriesRepository.getCountriesByOrderByPopulationAsc(); // TODO: list of countries sorted by population in the given order
                } else
                    countries = this.countriesRepository.getCountriesByOrderByPopulationDesc();
                break;
            case "medals":
            default:
                countries = this.countriesRepository.getCountriesByOrderByCodeAsc();// TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = this.goldMedalRepository.countByCountry(country.getName());// TODO: get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
