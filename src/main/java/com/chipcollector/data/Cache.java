package com.chipcollector.data;

import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Collection;
import com.chipcollector.domain.CollectionImpl;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Cache {

    private final List<String> typeCasinoComboBoxItems = new ArrayList<>();
    private final List<String> cityCasinoComboBoxItems = new ArrayList<>();
    private final List<String> stateCasinoComboBoxItems = new ArrayList<>();
    private final List<String> countryCasinoComboBoxItems = new ArrayList<>();
    private final List<String> denomChipComboBoxItems = new ArrayList<>();
    private final List<String> colorChipComboBoxItems = new ArrayList<>();
    private final List<String> moldChipComboBoxItems = new ArrayList<>();
    private final List<String> inlayChipComboBoxItems = new ArrayList<>();
    private final List<String> insertsChipComboBoxItems = new ArrayList<>();
    private final List<String> categoryChipComboBoxItems = new ArrayList<>();
    private final List<Casino> casinosChipComboBoxItems = new ArrayList<>();
    private final Collection collection = new CollectionImpl();

    private final Map<String, URL> treeIConURLs = new HashMap<>();

}
