package com.chipcollector.data;

import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import com.chipcollector.model.dashboard.CasinoBean;
import com.chipcollector.spring.MainProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.isNull;

@Component
@MainProfile
public class CasinoHandler {

    private PokerChipDAO pokerChipDAO;

    @Autowired
    public CasinoHandler(PokerChipDAO pokerChipDAO) {
        this.pokerChipDAO = pokerChipDAO;
    }

    public Casino getCasino(CasinoBean casinoBean) {
        if (isNull(casinoBean) || isNullOrEmpty(casinoBean.getName())) {
            return null;
        }

        return getCasinoFromCasinoBean(casinoBean)
                .orElseGet(() ->
                {
                    Location location = getLocationFromCasinoBean(casinoBean)
                            .orElseGet(() -> createNewLocation(casinoBean));

                    return createNewCasino(casinoBean, location);
                });
    }

    private Casino createNewCasino(CasinoBean casinoBean, Location location) {
        return Casino.builder()
                .closeDate(casinoBean.getClosedDate())
                .openDate(casinoBean.getOpenDate())
                .name(casinoBean.getName())
                .type(casinoBean.getType())
                .status(casinoBean.getStatus())
                .website(casinoBean.getWebsite())
                .location(location)
                .build();
    }

    private Location createNewLocation(CasinoBean casinoBean) {
        Optional<Country> country = getCountryFromCasinoBean(casinoBean);
        Location.LocationBuilder locationBuilder = Location.builder()
                .city(casinoBean.getCity())
                .state(casinoBean.getState());
        country.ifPresent(locationBuilder::country);
        return locationBuilder.build();
    }

    public Optional<Casino> getCasinoFromCasinoBean(CasinoBean casinoBean) {
        return pokerChipDAO.getCasinoFinder()
                .withName(casinoBean.getName())
                .withCity(casinoBean.getCity())
                .withState(casinoBean.getState())
                .withCountry(casinoBean.getCountryName())
                .findSingle();
    }

    protected Optional<Location> getLocationFromCasinoBean(CasinoBean casinoBean) {
        return pokerChipDAO.getLocationFinder()
                .withCity(casinoBean.getCity())
                .withState(casinoBean.getState())
                .withCountry(casinoBean.getCountryName())
                .findSingle();
    }

    protected Optional<Country> getCountryFromCasinoBean(CasinoBean casinoBean) {
        return getCountryFromName(casinoBean.getCountryName());
    }

    public Optional<Country> getCountryFromName(String name) {
        return pokerChipDAO.getCountry(name);
    }

    public Casino updateCasino(CasinoBean casinoBean) {
        Casino casino = casinoBean.getCasino();
        if (isNull(casino)) {
            return getCasino(casinoBean);
        } else if (casinoBean.isDirty()) {
            casino.setCloseDate(casinoBean.getClosedDate());
            casino.setOpenDate(casinoBean.getOpenDate());
            casino.setName(casinoBean.getName());
            casino.setStatus(casinoBean.getStatus());
            casino.setTheme(casinoBean.getTheme());
            casino.setType(casinoBean.getType());
            casino.setWebsite(casinoBean.getWebsite());
            Location location = getLocationFromCasinoBean(casinoBean)
                    .orElseGet(() -> createNewLocation(casinoBean));
            casino.setLocation(location);
        }
        return casino;
    }
}
