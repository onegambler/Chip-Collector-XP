package com.chipcollector.domain;

import lombok.experimental.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static javax.persistence.CascadeType.*;
import static lombok.AccessLevel.NONE;

@Entity
@Builder
@Getter
@Table(name = "POKER_CHIPS")
public class PokerChip {

    @Id
    private int id;
    @ManyToOne(optional = false, cascade = {PERSIST, REFRESH, MERGE})
    @JoinColumn(name = "CASINO_ID")
    private Casino casino;
    @Column(length = 50)
    private String denom;
    @Column(length = 4)
    private String year;
    @Column(length = 50)
    private String color;
    @Column(length = 50)
    private String inserts;
    @Column(length = 50)
    private String mold;
    @Column(unique = true, length = 10)
    private String tcrID;
    @Column(scale = 3)
    private int issue;
    @Column(length = 50)
    private String inlay;
    @Column(length = 4)
    private String rarity;
    @Column(length = 15)
    private String condition;
    @Column(length = 25)
    private String category;
    @Column(name = "ACQUISITION_DATE")
    private LocalDate acquisitionDate;
    @Column(length = 4000)
    private String notes;

    private MoneyAmount value;
    private MoneyAmount amountPaid;

    @Getter(NONE)
    @ManyToOne(cascade = {PERSIST, REFRESH, MERGE})
    @JoinColumn(name = "FRONT_IMAGE_ID")
    private BlobImage frontImage;

    @Getter(NONE)
    @ManyToOne(cascade = {PERSIST, REFRESH, MERGE})
    @JoinColumn(name = "BACK_IMAGE_ID")
    private BlobImage backImage;

    private boolean obsolete;

    @Transient
    private boolean imagesChanged;

    @Transient
    @Getter(NONE)
    private boolean dirty;

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

    public void setObsolete(boolean obsolete) {
        this.obsolete = updateDirty(this.obsolete, obsolete);
    }

    public void setFrontImage(BlobImage image) {
        this.frontImage = updateDirty(this.frontImage, image);
        imagesChanged = true;
    }

    public void setBackImage(BlobImage image) {
        this.backImage = updateDirty(this.backImage, image);
        imagesChanged = true;
    }

    public void resetDirty() {
        dirty = false;
        imagesChanged = false;
    }

    public Optional<BlobImage> getFrontImage() {
        return Optional.ofNullable(frontImage);
    }

    public Optional<BlobImage> getBackImage() {
        return Optional.ofNullable(backImage);
    }

    private <T> T updateDirty(T oldValue, T newValue) {
        dirty = nonNull(oldValue) ? oldValue.equals(newValue) : nonNull(newValue);
        return newValue;
    }

    @Override
    public String toString() {
        return this.tcrID;
    }

    public static String[] CHIP_CONDITIONS = new String[]{"Uncirculated", "Slightly Used", "Average", "Well Used", "Poor", "Cancelled"};
    public static String[] CHIP_CATEGORY = new String[]{"Baccarat", "Error", "Faro", "Free Play", "Match Play", "No Cash Value", "No Denomination", "Non-Negotiable", "Poker", "Roulette", "Race and Sport"};

}