package com.chipcollector.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true,
        format = {"pretty", "html:target/cucumber-report"},
        features = "classpath:features/",
        glue = "com.chipcollector.acceptance",
        tags = "~@wip, ~@ignore")
public class PokerChipXPAT {
}
