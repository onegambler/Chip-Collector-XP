package com.chipcollector.controllers.dashboard;

import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import javafx.scene.control.TreeItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CasinoTreeItem extends TreeItem<Object> {

    private Map<Long, TreeItem> countryMap = new HashMap<>();
    private Map<Long, TreeItem> cityMap = new HashMap<>();

    private TreeItem<Casino> other = new TreeItem<>();

    public CasinoTreeItem(List<Casino> casinos) {
        this.setValue("Casinos");
        for (Casino casino : casinos) {
            Optional<Location> location = casino.getLocation();
            if (location.isPresent()) {
                Country country = location.get().getCountry();
                countryMap.putIfAbsent(country.getId(), new TreeItem<>(casino.getName()));

            } else {
                other.getChildren().add(new TreeItem<>(casino));
            }
        }
    }
}
