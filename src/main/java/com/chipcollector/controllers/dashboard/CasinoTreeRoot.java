package com.chipcollector.controllers.dashboard;

import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import com.chipcollector.util.MessagesHelper;
import javafx.scene.control.TreeItem;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CasinoTreeRoot extends TreeItem<Object> {

    public CasinoTreeRoot(List<Casino> casinos) {
        this.setValue(MessagesHelper.getString(CASINO_STRING_KEY));

        final TreeItem<Object> others = new TreeItem<>();
        final Map<Long, Item> casinoTreeMap = new HashMap<>();

        for (Casino casino : casinos) {
            Optional<Location> location = casino.getLocation();
            if (location.isPresent()) {
                Country country = location.get().getCountry();
                String cityName = location.get().getCity();

                Item countryItem = casinoTreeMap.computeIfAbsent(country.getId(), id -> createCountryItem(country));
                Item cityItem = countryItem.getChildren().computeIfAbsent(cityName, name -> createCityItem(name, countryItem));

                cityItem.getNode().getChildren().add(new TreeItem<>(casino));

            } else {
                others.getChildren().add(new TreeItem<>(casino));
            }
        }

        this.getChildren().add(others);
    }

    private Item createCityItem(String cityName, Item countryItem) {
        Item cityItem = new Item(cityName);
        countryItem.getNode().getChildren().add(cityItem.getNode());
        return cityItem;
    }

    private Item createCountryItem(Country country) {
        Item countryItem = new Item(country.getName());
        this.getChildren().add(countryItem.getNode());
        return countryItem;
    }

    @Getter
    private class Item {
        private final TreeItem<Object> node;
        private final Map<String, Item> children = new HashMap<>();

        public Item(@NonNull Object nodeValue) {
            this.node = new TreeItem<>(nodeValue);
        }

        @Override
        public String toString() {
            return node.toString();
        }
    }

    private static final String CASINO_STRING_KEY = "domain.casino";
}
