Feature: Some terse yet descriptive text of what is desired
  In order to realize a named business value
  As an explicit system actor
  I want to gain some beneficial outcome which furthers the goal

  Scenario: A user wants to add a new poker chip to the system by searching it online.
    Given the Chip Collector XP App is open
    When a user click on the 'Add Poker Chip' button
    And enters a search parameter "Bellagio" in the opened search dialog
    And clicks on the first casino
    And clicks on the first poker chip value
    And clicks on OK button in the newly opened 'Add Poker Chip Dialog' and closes the search dialog
    Then poker chip table contains the newly created poker chip
    And a new Poker Chip is stored into the database

  Scenario: A user wants to manually add a new poker chip to the system.
    Given the Chip Collector XP App is open
    When a user click on the 'Manually sdd Poker Chip' button
    And fills all details in the opened 'Add Poker Chip Dialog'
    And clicks on OK button in the newly opened 'Add Poker Chip Dialog'
    Then poker chip table contains the newly created poker chip
    And a new Poker Chip is stored into the database