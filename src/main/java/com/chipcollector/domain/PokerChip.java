package com.chipcollector.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;

import static java.util.Objects.nonNull;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Entity
@Table(name = "POKER_CHIP")
@Builder
public class PokerChip {

    @Id
    @Getter
    private int id;
    @Getter
    @ManyToOne(optional = false, cascade = {PERSIST, MERGE})
    private Casino casino;
    @Getter
    @Column(length = 50)
    private String denom;
    @Getter
    @Column(length = 4)
    private String year;
    @Getter
    @Column(length = 50)
    private String color;
    @Getter
    @Column(length = 50)
    private String inserts;
    @Getter
    @Column(length = 50)
    private String mold;
    @Getter
    @Column(unique = true, length = 10)
    private String tcrID;
    @Getter
    @Column(scale = 3)
    private int issue;
    @Getter
    @Column(length = 50)
    private String inlay;
    @Getter
    @Column(length = 4)
    private String rarity;
    @Getter
    @Column(length = 15)
    private String condition;
    @Getter
    @Column(length = 25)
    private String category;
    @Getter
    @Column(name = "ACQUISITION_DATE")
    private LocalDate acquisitionDate;
    @Getter
    @Column(length = 4000)
    private String notes;
    @Getter
    private MoneyAmount value;
    @Getter
    private MoneyAmount amountPaid;
    @Getter
    private boolean obsolete;

    @Transient
    private ImagesProxy images;

    @Getter
    @Setter
    @Transient
    private boolean frontImageChanged;
    @Getter
    @Setter
    @Transient
    private boolean backImageChanged;
    @Transient
    private boolean dirty;
    @Transient
    private int tableIndex = -1;

    public BufferedImage[] getImages() throws Exception {
        if (images != null) {
            return images.getImages();
        }

        return null;
    }

    public void setCasino(Casino casino) {
        this.casino = updateDirty(this.casino, casino);
    }

    public void setId(int id) {
        this.id = updateDirty(this.id, id);
    }

    public void setDenom(String denom) {
        this.denom = updateDirty(this.denom, denom);
    }

    public void setYear(String year) {
        this.year = updateDirty(this.year, year);
    }

    public void setColor(String color) {
        this.color = updateDirty(this.color, color);
    }

    public void setInserts(String inserts) {
        this.inserts = updateDirty(this.inserts, inserts);
    }

    public void setMold(String mold) {
        this.mold = updateDirty(this.mold, mold);
    }

    public void setTcrID(String tcrID) {
        this.tcrID = updateDirty(this.tcrID, tcrID);
    }

    public void setIssue(int issue) {
        this.issue = updateDirty(this.issue, issue);
    }

    public void setInlay(String inlay) {
        this.inlay = updateDirty(this.inlay, inlay);
    }

    public void setRarity(String rarity) {
        this.rarity = updateDirty(this.rarity, rarity);
    }

    public void setCondition(String condition) {
        this.condition = updateDirty(this.condition, condition);
    }

    public void setCategory(String category) {
        this.category = updateDirty(this.category, category);
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = updateDirty(this.acquisitionDate, acquisitionDate);
    }

    public void setNotes(String notes) {
        this.notes = updateDirty(this.notes, notes);
    }

    public void setValue(MoneyAmount value) {
        this.value = updateDirty(this.value, value);
    }

    public void setAmountPaid(MoneyAmount amountPaid) {
        this.amountPaid = updateDirty(this.amountPaid, amountPaid);
    }

    public void setImages(BufferedImage frontImage, BufferedImage backImage) {
        images.setImages(frontImage, backImage);
        this.dirty = true;
    }

    public void setImagesLoader(ImagesProxy images) {
        this.images = updateDirty(this.images, images);
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = updateDirty(this.obsolete, obsolete);
    }

    public void resetDirty() {
        dirty = false;
        frontImageChanged = false;
        backImageChanged = false;
    }

    private <T> T updateDirty(T oldValue, T newValue) {

        dirty = nonNull(oldValue) ? oldValue.equals(newValue) : nonNull(newValue);

        return newValue;
    }

    @Override
    public String toString() {
        return this.tcrID;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    public static String[] CHIP_CONDITIONS = new String[]{"Uncirculated", "Slightly Used", "Average", "Well Used", "Poor", "Cancelled"};
    public static String[] CHIP_CATEGORY = new String[]{"Baccarat", "Error", "Faro", "Free Play", "Match Play", "No Cash Value", "No Denomination", "Non-Negotiable", "Poker", "Roulette", "Race and Sport"};

}