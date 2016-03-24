package com.chipcollector.controller.dashboard;

import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import javafx.scene.control.TreeItem;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class CasinoTreeRootTest {

    @Test
    public void casinoTreeRootGetsConstructedCorrectly() {
        List<Casino> casinoList = singletonList(Casino.builder().name("casinoName").build());
        CasinoTreeRoot underTest = new CasinoTreeRoot(casinoList);

        assertThat(underTest.getValue()).isEqualTo("All Casinos");
    }

    @Test
    public void whenCasinoDoesNotContainLocationThenAddCasinoToOtherNode() {
        List<Casino> casinoList = singletonList(Casino.builder().name("casinoName").build());
        CasinoTreeRoot underTest = new CasinoTreeRoot(casinoList);
        assertThat(underTest.getChildren()).hasSize(1);
        final TreeItem<Object> otherNode = underTest.getChildren().get(0);
        assertThat(otherNode.getValue()).isEqualTo("Other");
        assertThat(otherNode.getChildren()).extracting(TreeItem::getValue).hasSameElementsAs(casinoList);
    }

    @Test
    public void whenCasinoContainsLocationThenAddCasinoToRoot() {
        Country country = Country.builder().name("Italy").flagImageName("IT.png").build();
        Location casinoLocation = Location.builder().country(country).city("San Marino").build();
        Casino casino = Casino.builder().name("casinoName").location(casinoLocation).build();
        List<Casino> casinoList = singletonList(casino);
        CasinoTreeRoot underTest = new CasinoTreeRoot(casinoList);
        List<TreeItem<Object>> countryNodeList = underTest.getChildren();
        assertThat(countryNodeList).extracting(TreeItem::getValue).containsOnly("Italy");
        List<TreeItem<Object>> cityNodeList = countryNodeList.get(0).getChildren();
        assertThat(cityNodeList).extracting(TreeItem::getValue).containsOnly("San Marino");

        List<TreeItem<Object>> casinoNodeList = cityNodeList.get(0).getChildren();
        assertThat(casinoNodeList).extracting(TreeItem::getValue)
                .containsExactly(casino);

        assertThat(casinoNodeList.get(0).isLeaf()).isTrue();
    }
}