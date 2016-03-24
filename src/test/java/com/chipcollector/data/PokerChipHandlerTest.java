package com.chipcollector.data;

import com.chipcollector.domain.PokerChip;
import com.chipcollector.model.dashboard.PokerChipBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.chipcollector.test.util.PokerChipTestUtil.createTestPokerChip;
import static com.chipcollector.util.ImageConverter.getImageFromByteArray;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PokerChipHandlerTest {

    @Mock
    private CasinoHandler casinoHandler;
    private PokerChipHandler pokerChipHandler;

    @Before
    public void setUp() {
        pokerChipHandler = new PokerChipHandler(casinoHandler);
    }

    @Test
    public void getNewPokerChipCreatesANewPokerChipObjectFromPokerChipBean() {
        final PokerChip expectedPokerChip = createTestPokerChip();
        when(casinoHandler.getCasino(any())).thenReturn(expectedPokerChip.getCasino());
        PokerChipBean pokerChipBean = new PokerChipBean(expectedPokerChip);
        final PokerChip newPokerChip = pokerChipHandler.getNewPokerChip(pokerChipBean);
        assertThat(newPokerChip).isEqualTo(expectedPokerChip);
        assertThat(newPokerChip.getFrontImage()).isPresent();
        assertThat(newPokerChip.getBackImage()).isPresent();
        assertThat(newPokerChip.getFrontImage().get()).isNotSameAs(newPokerChip.getBackImage().get());
    }

    @Test
    public void getNewPokerChipWithoutImagesCreatesANewPokerChipObjectFromPokerChipBeanWithNullImages() {
        final PokerChip expectedPokerChip = createTestPokerChip();
        expectedPokerChip.setFrontImage(null);
        expectedPokerChip.setBackImage(null);
        when(casinoHandler.getCasino(any())).thenReturn(expectedPokerChip.getCasino());
        PokerChipBean pokerChipBean = new PokerChipBean(expectedPokerChip);
        final PokerChip newPokerChip = pokerChipHandler.getNewPokerChip(pokerChipBean);
        assertThat(newPokerChip.getFrontImage()).isEmpty();
        assertThat(newPokerChip.getBackImage()).isEmpty();
    }

    @Test
    public void getNewPokerChipWithSameImageCreatesANewPokerChipObjectFromPokerChipBeanWithSameBlobImage() {
        final PokerChip expectedPokerChip = createTestPokerChip();
        when(casinoHandler.getCasino(any())).thenReturn(expectedPokerChip.getCasino());
        PokerChipBean pokerChipBean = new PokerChipBean(expectedPokerChip);
        pokerChipBean.backImageProperty().set(pokerChipBean.getFrontImage());
        final PokerChip newPokerChip = pokerChipHandler.getNewPokerChip(pokerChipBean);
        assertThat(newPokerChip.getFrontImage().get()).isSameAs(newPokerChip.getBackImage().get());
    }

    @Test
    public void updatePokerChipUpdatePokerChipsFromPokerChipBean() {
        PokerChip expectedPokerChip = createTestPokerChip();
        PokerChipBean pokerChipBean = new PokerChipBean(PokerChip.builder().build());
        fillPokerChipBeanValues(expectedPokerChip, pokerChipBean);

        when(casinoHandler.updateCasino(any())).thenReturn(expectedPokerChip.getCasino());
        PokerChip result = pokerChipHandler.getUpdatedPokerChip(pokerChipBean);
        assertThat(result).isEqualTo(expectedPokerChip);
    }

    @Test
    public void updatePokerChipWithoutImagesUpdatesPokerChipObjectFromPokerChipBeanWithNullImages() {
        PokerChip expectedPokerChip = createTestPokerChip();
        PokerChipBean pokerChipBean = new PokerChipBean(PokerChip.builder().build());
        fillPokerChipBeanValues(expectedPokerChip, pokerChipBean);
        pokerChipBean.setFrontImage(null);
        pokerChipBean.setBackImage(null);
        when(casinoHandler.getCasino(any())).thenReturn(expectedPokerChip.getCasino());
        final PokerChip newPokerChip = pokerChipHandler.getUpdatedPokerChip(pokerChipBean);
        assertThat(newPokerChip.getFrontImage()).isEmpty();
        assertThat(newPokerChip.getBackImage()).isEmpty();
    }

    @Test
    public void updatePokerChipWithSameImageUpdatesPokerChipObjectFromPokerChipBeanWithSameBlobImage() {
        PokerChip expectedPokerChip = createTestPokerChip();
        PokerChipBean pokerChipBean = new PokerChipBean(PokerChip.builder().build());
        fillPokerChipBeanValues(expectedPokerChip, pokerChipBean);
        when(casinoHandler.getCasino(any())).thenReturn(expectedPokerChip.getCasino());
        pokerChipBean.setBackImage(pokerChipBean.getFrontImage());
        final PokerChip newPokerChip = pokerChipHandler.getUpdatedPokerChip(pokerChipBean);
        assertThat(newPokerChip.getFrontImage().get()).isSameAs(newPokerChip.getBackImage().get());
    }

    private void fillPokerChipBeanValues(PokerChip expectedPokerChip, PokerChipBean pokerChipBean) {
        pokerChipBean.setCancelled(expectedPokerChip.isCancelled());
        pokerChipBean.setCategory(expectedPokerChip.getCategory());
        pokerChipBean.setColor(expectedPokerChip.getColor());
        pokerChipBean.setCondition(expectedPokerChip.getCondition());
        pokerChipBean.setDateOfAcquisition(expectedPokerChip.getAcquisitionDate());
        pokerChipBean.setDenom(expectedPokerChip.getDenom());
        pokerChipBean.setInlay(expectedPokerChip.getInlay());
        pokerChipBean.setInserts(expectedPokerChip.getInserts());
        pokerChipBean.setIssue(expectedPokerChip.getIssue());
        pokerChipBean.setMold(expectedPokerChip.getMold());
        pokerChipBean.setNotes(expectedPokerChip.getNotes());
        pokerChipBean.setObsolete(expectedPokerChip.isObsolete());
        pokerChipBean.setRarity(expectedPokerChip.getRarity());
        pokerChipBean.setTcrId(expectedPokerChip.getTcrID());
        pokerChipBean.setAmountValue(expectedPokerChip.getAmountValue());
        pokerChipBean.setAmountPaid(expectedPokerChip.getAmountPaid());
        pokerChipBean.setYear(expectedPokerChip.getYear());
        pokerChipBean.getCasinoBean().setCity(expectedPokerChip.getCasino().getCity());
        pokerChipBean.getCasinoBean().setClosedDate(expectedPokerChip.getCasino().getCloseDate());
        pokerChipBean.getCasinoBean().setCountry(expectedPokerChip.getCasino().getCountry());
        pokerChipBean.getCasinoBean().setName(expectedPokerChip.getCasino().getName());
        pokerChipBean.getCasinoBean().setOpenDate(expectedPokerChip.getCasino().getOpenDate());
        pokerChipBean.getCasinoBean().setOpenDate(expectedPokerChip.getCasino().getOpenDate());
        pokerChipBean.getCasinoBean().setOldName(expectedPokerChip.getCasino().getOldName());
        pokerChipBean.getCasinoBean().setOpenDate(expectedPokerChip.getCasino().getOpenDate());
        pokerChipBean.getCasinoBean().setState(expectedPokerChip.getCasino().getState());
        pokerChipBean.getCasinoBean().setStatus(expectedPokerChip.getCasino().getStatus());
        pokerChipBean.getCasinoBean().setTheme(expectedPokerChip.getCasino().getTheme());
        pokerChipBean.getCasinoBean().setType(expectedPokerChip.getCasino().getType());
        pokerChipBean.getCasinoBean().setWebsite(expectedPokerChip.getCasino().getWebsite());
        pokerChipBean.setFrontImage(getImageFromByteArray(expectedPokerChip.getFrontImage().get().getImage()));
        pokerChipBean.setBackImage(getImageFromByteArray(expectedPokerChip.getBackImage().get().getImage()));
    }
}