package com.chipcollector.views.node.moneytextfield;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyFilterTest {

    private MoneyFilter moneyFilter = new MoneyFilter();

    @Test(expected = NullPointerException.class)
    public void whenAmountIsNullThenMatchesThrowsNullPointerException() {
        moneyFilter.matches(null);
    }

    @Test
    public void whenAmountIsAllNumbersThenReturnsTrue() {
        assertThat(moneyFilter.matches("44232")).isTrue();
    }

    @Test
    public void whenAmountContainsLettersThenReturnsFalse() {
        assertThat(moneyFilter.matches("442asd32")).isFalse();
    }

    @Test
    public void whenAmountContainsCurrencyAsStartingCharacterFollowedOnlyByNumbersThenReturnsTrue() {
        assertThat(moneyFilter.matches("$1230")).isTrue();
    }

    @Test
    public void whenAmountContainsCurrencyAsNotStartingCharacterFollowedOnlyByNumbersThenReturnsFalse() {
        assertThat(moneyFilter.matches("$123$0")).isFalse();
    }

    @Test
    public void whenAmountContainsCorrectCommasAndColonsThenReturnsTrue() {
        assertThat(moneyFilter.matches("$ 1.230.000,22")).isTrue();
    }
}