package com.chipcollector.acceptance.steps;

import com.chipcollector.data.PokerChipCollection;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Objects;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.testfx.api.FxAssert.verifyThat;


@ContextConfiguration(classes = com.chipcollector.util.TestAppConfig.class)
@ActiveProfiles("test")
public class PokerChipDialogStep {

    @Autowired
    private PokerChipCollection collection;

    @Autowired
    private PokerChipDialogStepHelper helper;

    @Given("^the Chip Collector XP App is open$")
    public void thePokerChipDialogIsOpen() throws Throwable {
        verifyThat(POKER_CHIP_DIALOG, Node::isVisible);
    }

    @When("^a user click on the 'Add Poker Chip' button$")
    public void aUserClickOnTheCancelButton() throws Throwable {
        helper.clickOn("#cancelButton");
    }

    @Then("^the 'Poker Chip Dialog' opens$")
    public void theDialogCloses() throws Throwable {
        verifyThat(POKER_CHIP_DIALOG, Objects::isNull);
    }
    public static final String YEAR_TEXT_FIELD = "#yearTextField";
    public static final String POKER_CHIP_DIALOG = "#pokerChipDialog";
}
