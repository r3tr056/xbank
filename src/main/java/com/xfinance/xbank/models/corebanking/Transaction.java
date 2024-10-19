package com.xfinance.xbank.models.corebanking;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="transation", schema="xbank")

@SequenceGenerator(name="transation_seq", sequenceName="transation_sequence", schema="xbank", initialValue=5)
public class Transaction {
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="transation_seq")
    private long id;

    // Source Account ID
    private long sourceAccountId;
    // Target Account ID
    private long targetAccountId;

    // Target account name
    private String targetOwnerName;

    private double amount;
    private Amount amntSystem;

    private LocalDateTime initiationDate;
    private LocalDateTime completionDate;

    private String reference;
    private String txnProcessor;

    private TransactionDetails txnDetails;
    private TransactionStatus txnStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long setSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public long getTragetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public String getTargetOwnerName() {
        return targetOwnerName;
    }

    public void setTargetOwnerName(String targetOwnerName) {
        this.targetOwnerName = targetOwnerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getInitiationDate() {
        return initiationDate;
    }

    public void setInitiationDate(LocalDateTime initiationDate) {
        this.initiationDate = initiationDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void getLongitude() {
        return longitude
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Transations{" + "sourceAccountId=" + sourceAccountId + ", targetAccountId=" + targetAccountId + ", targetOwnerName='" + targetOwnerName + '\'' + ", amount=" + amount + ", initiationDate=" + initiationDate + ", completionDate=" + completionDate + ", reference='" + reference + '\'' + '}';
    }
}