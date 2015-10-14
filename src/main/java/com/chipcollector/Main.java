package com.chipcollector;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;

import java.util.List;

public class Main {

    public static void main(String [] args) {

        EbeanServer defaultServer = Ebean.getDefaultServer();
        Ebean.register(defaultServer, true);
        PokerChipDAO dao = new PokerChipDAO(defaultServer);

        Country country = new Country();
        country.setName("Pollo");

        Location location = new Location();
        location.setCity("Citta");
        location.setCountry(country);
        location.setState("stato");

        Ebean.save(location);

        location = new Location();
        location.setCity("Citta2");
        location.setCountry(country);

        location.setState("stato2");

        Ebean.save(location);

        List<Location> b = Ebean.find(Location.class).findList();
        System.out.println(b.get(0).getCountry() == b.get(1).getCountry());

    }
}
