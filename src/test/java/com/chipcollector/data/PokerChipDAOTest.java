package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.chipcollector.domain.PokerChip;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PokerChipDAOTest {

    @Mock
    private EbeanServer ebeanServer;

    private PokerChipDAO underTest;

    @Before
    public void setUp() {
        underTest = new PokerChipDAO(ebeanServer);
    }

    @Test
    public void createPokerChipFilterWorksAsExpected() {
        underTest.createPokerChipFilter();
        verify(ebeanServer).createQuery(PokerChip.class);

    }

}