package com.chipcollector.data;

import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.spring.MainProfile;
import com.google.common.base.Throwables;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.chipcollector.util.ImageConverter.getBlobImageFromImage;
import static java.util.Objects.isNull;

@Component
@MainProfile
public class PokerChipHandler {

    private CasinoHandler casinoHandler;

    @Autowired
    public PokerChipHandler(CasinoHandler casinoHandler) {
        this.casinoHandler = casinoHandler;
    }

    public PokerChip getNewPokerChip(PokerChipBean pokerChipBean) {
        Casino casino = casinoHandler.getCasino(pokerChipBean.getCasinoBean());

        PokerChip.PokerChipBuilder pokerChipBuilder = PokerChip.builder()
                .category(pokerChipBean.categoryProperty().get())
                .acquisitionDate(pokerChipBean.dateOfAcquisitionProperty().get())
                .color(pokerChipBean.colorProperty().get())
                .inlay(pokerChipBean.inlayProperty().get())
                .condition(pokerChipBean.conditionProperty().get())
                .denom(pokerChipBean.denomProperty().get())
                .inserts(pokerChipBean.insertsProperty().get())
                .mold(pokerChipBean.moldProperty().get())
                .notes(pokerChipBean.notesProperty().get())
                .obsolete(pokerChipBean.obsoleteProperty().get())
                .cancelled(pokerChipBean.cancelledProperty().get())
                .rarity(pokerChipBean.rarityProperty().get())
                .tcrID(pokerChipBean.tcrIdProperty().get())
                .year(pokerChipBean.yearProperty().get())
                .issue(pokerChipBean.issueProperty().get())
                .amountPaid(pokerChipBean.amountPaidProperty().get())
                .amountValue(pokerChipBean.amountValueProperty().get())
                .casino(casino);

        Image frontImage = pokerChipBean.frontImageProperty().get();
        BlobImage frontBlobImage = null;
        if (frontImage != null) {
            frontBlobImage = getBlobImageFromImageAndHandleException(frontImage);
            pokerChipBuilder.frontImage(frontBlobImage);
        }

        Image backImage = pokerChipBean.backImageProperty().get();
        if (backImage != null) {
            if (backImage.equals(frontImage)) {
                pokerChipBuilder.backImage(frontBlobImage);
            } else {
                pokerChipBuilder.backImage(getBlobImageFromImageAndHandleException(backImage));
            }
        }
        return pokerChipBuilder.build();
    }

    public PokerChip getUpdatedPokerChip(PokerChipBean pokerChipBean) {
        final PokerChip pokerChip = pokerChipBean.getPokerChip();
        pokerChip.setCancelled(pokerChipBean.cancelledProperty().get());
        pokerChip.setCategory(pokerChipBean.categoryProperty().get());
        pokerChip.setColor(pokerChipBean.colorProperty().get());
        pokerChip.setDenom(pokerChipBean.denomProperty().get());
        pokerChip.setInlay(pokerChipBean.inlayProperty().get());
        pokerChip.setInserts(pokerChipBean.insertsProperty().get());
        pokerChip.setMold(pokerChipBean.moldProperty().get());
        pokerChip.setYear(pokerChipBean.yearProperty().get());
        pokerChip.setTcrID(pokerChipBean.tcrIdProperty().get());
        pokerChip.setObsolete(pokerChipBean.obsoleteProperty().get());
        pokerChip.setCondition(pokerChipBean.conditionProperty().get());
        pokerChip.setRarity(pokerChipBean.rarityProperty().get());
        pokerChip.setAmountValue(pokerChipBean.amountValueProperty().get());
        pokerChip.setAmountPaid(pokerChipBean.amountPaidProperty().get());
        pokerChip.setIssue(pokerChipBean.issueProperty().get());
        pokerChip.setNotes(pokerChipBean.notesProperty().get());
        pokerChip.setAcquisitionDate(pokerChipBean.dateOfAcquisitionProperty().get());
        if (pokerChipBean.isFrontImageChanged()) {
            pokerChip.setFrontImage(getBlobImageFromImageAndHandleException(pokerChipBean.getFrontImage()));
        }
        if (pokerChipBean.isBackImageChanged()) {
            if (areFrontAndBackImageSame(pokerChipBean)) {
                pokerChip.setBackImage(pokerChip.getFrontImage().orElse(null));
            } else {
                pokerChip.setBackImage(getBlobImageFromImageAndHandleException(pokerChipBean.getFrontImage()));
            }
        }

        if (pokerChipBean.getCasinoBean().isDirty()) {
            Casino casino = casinoHandler.updateCasino(pokerChipBean.getCasinoBean());
            pokerChip.setCasino(casino);
        }
        return pokerChip;
    }

    private BlobImage getBlobImageFromImageAndHandleException(Image image) {
        try {
            return isNull(image) ? null : getBlobImageFromImage(image);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private boolean areFrontAndBackImageSame(PokerChipBean bean) {
        return bean.frontImageProperty().getValue() == bean.backImageProperty().getValue();
    }
}
