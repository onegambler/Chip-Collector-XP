package com.chipcollector.domain;

import com.avaje.ebean.annotation.EmbeddedColumns;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static javax.persistence.CascadeType.*;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;

@Data
@Entity
@Builder
@Table(name = "POKER_CHIPS")
public class PokerChip {

    @Id
    @Setter(NONE)
    private Long id;

    @ManyToOne(cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "CASINO_ID")
    private Casino casino;

    @Column
    private String denom;

    @Column
    private String year;

    @Column
    private String color;

    @Column
    private String inserts;

    @Column
    private String mold;

    @Column(unique = true)
    private String tcrID;

    @Column
    private String issue;

    @Column
    private String inlay;

    @Column
    private Rarity rarity;

    @Column
    private String condition;

    @Column
    private String category;

    @Column(name = "ACQUISITION_DATE")
    private LocalDate acquisitionDate;

    @Column
    private String notes;

    @Embedded
    @EmbeddedColumns(columns = "currency=valueCurrency, amount=value")
    private MoneyAmount amountValue;

    @Embedded
    @EmbeddedColumns(columns = "currency=paidCurrency, amount=paid")
    private MoneyAmount amountPaid;

    @Getter(NONE)
    @ManyToOne(cascade = {PERSIST, REFRESH, MERGE})
    @JoinColumn(name = "FRONT_IMAGE_ID")
    private BlobImage frontImage;

    @Getter(NONE)
    @ManyToOne(cascade = {PERSIST, REFRESH, MERGE})
    @JoinColumn(name = "BACK_IMAGE_ID")
    private BlobImage backImage;

    @Setter(NONE)
    private boolean obsolete;
    @Setter(NONE)
    private boolean cancelled;

    @Transient
    private boolean imagesChanged;

    @Transient
    @Setter(NONE)
    @Getter(PACKAGE)
    private boolean dirty;

    public void setCasino(Casino casino) {
        this.casino = updateDirty(this.casino, casino);
    }

    public void setId(long id) {
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

    public void setIssue(String issue) {
        this.issue = updateDirty(this.issue, issue);
    }

    public void setInlay(String inlay) {
        this.inlay = updateDirty(this.inlay, inlay);
    }

    public void setRarity(Rarity rarity) {
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

    public void setAmountValue(MoneyAmount amountValue) {
        this.amountValue = updateDirty(this.amountValue, amountValue);
    }

    public void setAmountPaid(MoneyAmount amountPaid) {
        this.amountPaid = updateDirty(this.amountPaid, amountPaid);
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = updateDirty(this.obsolete, obsolete);
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = updateDirty(this.cancelled, cancelled);
    }

    public void setFrontImage(BlobImage image) {
        this.frontImage = image;
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
        dirty = nonNull(oldValue) ? !oldValue.equals(newValue) : nonNull(newValue);
        return newValue;
    }

    @Override
    public String toString() {
        return this.tcrID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokerChip pokerChip = (PokerChip) o;
        return obsolete == pokerChip.obsolete &&
                cancelled == pokerChip.cancelled &&
                Objects.equals(id, pokerChip.id) &&
                Objects.equals(casino, pokerChip.casino) &&
                Objects.equals(denom, pokerChip.denom) &&
                Objects.equals(year, pokerChip.year) &&
                Objects.equals(color, pokerChip.color) &&
                Objects.equals(inserts, pokerChip.inserts) &&
                Objects.equals(mold, pokerChip.mold) &&
                Objects.equals(tcrID, pokerChip.tcrID) &&
                Objects.equals(issue, pokerChip.issue) &&
                Objects.equals(inlay, pokerChip.inlay) &&
                rarity == pokerChip.rarity &&
                Objects.equals(condition, pokerChip.condition) &&
                Objects.equals(category, pokerChip.category) &&
                Objects.equals(acquisitionDate, pokerChip.acquisitionDate) &&
                Objects.equals(notes, pokerChip.notes) &&
                Objects.equals(amountValue, pokerChip.amountValue) &&
                Objects.equals(amountPaid, pokerChip.amountPaid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, casino, denom, year, color, inserts, mold, tcrID, issue, inlay, rarity, condition,
                category, acquisitionDate, notes, amountValue, amountPaid, obsolete, cancelled);
    }
}