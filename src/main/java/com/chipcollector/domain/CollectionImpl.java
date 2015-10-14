package com.chipcollector.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class CollectionImpl implements Collection {

    private List<PokerChip> pokerChipList;
    private Map<Long, Casino> casinoMap;
    private Map<Long, Location> locationsMap;
    private Map<Long, Country> countriesMap;

    public CollectionImpl() {
        casinoMap = new HashMap<>();
        locationsMap = new HashMap<>();
        countriesMap = new HashMap<>();
        pokerChipList = new ArrayList<>();
    }

    public void setCasinos(List<Casino> casinos) {
        requireNonNull(casinos, "Casinos list cannot be null");
        casinoMap = casinos.parallelStream()
                .collect(toMap(Casino::getId, identity()));
    }

    public void setLocations(List<Location> locations) {
        requireNonNull(locations, "Location list cannot be null");
        locationsMap = locations.parallelStream()
                .collect(toMap(Location::getId, identity()));
    }

    public void setCountries(List<Country> countries) {
        requireNonNull(countries, "Countries list cannot be null");
        countriesMap = countries.parallelStream()
                .collect(toMap(Country::getId, identity()));
    }
}