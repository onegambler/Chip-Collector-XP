package com.chipcollector.acceptance.steps;

import com.chipcollector.data.PokerChipCollection;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.api.FxAssert.verifyThat;

@ContextConfiguration(classes = com.chipcollector.util.TestAppConfig.class)
public class ChipCollectorStep {

    @Autowired
    private PokerChipCollection collection;

    @Autowired
    private JavaFXTestHelper helper;

    @Given("^the Chip Collector XP App is open$")
    public void theChipCollectorAppIsOpen() throws Throwable {
        verifyThat(MAIN_WINDOW, Node::isVisible);
    }

    @When("^a user click on the 'Add Poker Chip' button$")
    public void aUserClickOnTheCancelButton() throws Throwable {
        helper.clickOn("#addMenuButton");
    }

    @Then("^the 'Poker Chip Dialog' opens$")
    public void theDialogCloses() throws Throwable {
        verifyThat(SEARCH_POKER_CHIP_DIALOG, Node::isVisible);
    }

    @And("^enters a search parameter \"([^\"]*)\"$")
    public void entersASearchParameter(String casino) throws Throwable {
        helper.clickOn("#searchTextField").write(casino).clickOn("#searchButton");
        helper.sleep(2000);
    }

    @And("^clicks on the first casino$")
    public void clicksOnTheFirstCasino() throws Throwable {
        helper.clickOn("#casinosTableView");
        helper.sleep(2000);
    }

    @And("^clicks on the first poker chip value$")
    public void clicksOnTheFirstPokerChipValue() throws Throwable {
        helper.doubleClickOn("#pokerChipsTableView");
    }

    @And("^clicks on OK button in the newly opened 'Add Poker Chip Dialog'$")
    public void clicksOnOKButtonInTheNewlyOpenedAddPokerChipDialog() throws Throwable {
        helper.clickOn("#okButton").closeCurrentWindow();
    }

    @Then("^poker chip table contains the newly created poker chip$")
    public void pokerChipTableContainsTheNewlyCreatedPokerChip() throws Throwable {
        helper.clickOn("#showAllButton");
        verifyThat("#pokerChipTableView", input -> ((TableView) input).getItems().size() == 1);
    }

    @And("^a new Poker Chip is stored into the database$")
    public void aNewPokerChipIsStoredIntoTheDatabase() throws Throwable {
        assertThat(collection.getAllPokerChipsCount()).isEqualTo(1);
    }


    public static final String YEAR_TEXT_FIELD = "#yearTextField";
    public static final String MAIN_WINDOW = "#MainWindow";
    public static final String SEARCH_POKER_CHIP_DIALOG = "#searchPokerChipDialog";
}
