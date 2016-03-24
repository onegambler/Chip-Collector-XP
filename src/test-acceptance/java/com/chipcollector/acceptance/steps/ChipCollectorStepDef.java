package com.chipcollector.acceptance.steps;

import com.chipcollector.acceptance.util.AcceptanceTestAppConfig;
import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.domain.PokerChip;
import com.google.common.base.Predicate;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static com.chipcollector.test.util.PokerChipTestUtil.createTestPokerChip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.api.FxAssert.verifyThat;

@ContextConfiguration(classes = AcceptanceTestAppConfig.class)
public class ChipCollectorStepDef {

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

    @And("^enters a search parameter \"([^\"]*)\" in the opened search dialog$")
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
        helper.clickOn("#okButton");
    }

    @And("^clicks on OK button in the newly opened 'Add Poker Chip Dialog' and closes the search dialog$")
    public void clicksOnOKButtonInTheNewlyOpenedAddPokerChipDialogAndClosesSearchDialog() throws Throwable {
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

    @When("^a user click on the 'Manually sdd Poker Chip' button$")
    public void aUserClickOnTheManuallySddPokerChipButton() throws Throwable {
        final Node node = helper.from(helper.lookup("#addMenuButton"))
                .lookup((Predicate<Node>) input -> input.getStyleClass().contains("arrow-button"))
                .queryFirst();
        helper.clickOn(node).clickOn("#manualAddMenuItem");
    }

    @And("^fills all details in the opened 'Add Poker Chip Dialog'$")
    public void fillsAllDetailsInTheNewlyOpenedAddPokerChipDialog() throws Throwable {
        PokerChip pokerChip = createTestPokerChip();
        helper.clickOn(YEAR_TEXT_FIELD).write(pokerChip.getYear());
        helper.clickOn("#denomComboBox").write(pokerChip.getDenom());
        helper.clickOn("#issueTextField").write(pokerChip.getIssue());
        helper.clickOn("#colorComboBox").write(pokerChip.getColor());
        helper.clickOn("#moldComboBox").write(pokerChip.getMold());
        helper.clickOn("#inlayComboBox").write(pokerChip.getInlay());
        helper.clickOn("#insertsTextField").write(pokerChip.getInserts());
        helper.clickOn("#conditionComboBox").clickOn(pokerChip.getCondition());
        helper.clickOn("#categoryComboBox").clickOn(pokerChip.getCategory());
        helper.clickOn("#rarityComboBox").clickOn(pokerChip.getRarity().getDisplayName());
        helper.clickOn("#valueTextField").write(pokerChip.getAmountValue().toString());
        helper.clickOn("#acquisitionDatePicker").type(KeyCode.BACK_SPACE, 15).write(pokerChip.getAcquisitionDate().toString());
        helper.clickOn("#tcrTextField").write(pokerChip.getTcrID());
        helper.clickOn("#notesTextArea").write(pokerChip.getNotes());
        helper.clickOn("#paidTextField").write(pokerChip.getAmountPaid().toString());
        if (pokerChip.isObsolete()) {
            helper.clickOn("#obsoleteToggleButton");
        }
        if (pokerChip.isCancelled()) {
            helper.clickOn("#cancelledToggleButton");
        }
    }

    public static final String YEAR_TEXT_FIELD = "#yearTextField";
    public static final String MAIN_WINDOW = "#MainWindow";
    public static final String SEARCH_POKER_CHIP_DIALOG = "#searchPokerChipDialog";
}
