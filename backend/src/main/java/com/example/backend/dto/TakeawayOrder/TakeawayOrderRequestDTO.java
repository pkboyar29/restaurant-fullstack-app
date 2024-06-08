package com.example.backend.dto.TakeawayOrder;

import java.time.LocalDateTime;

public class TakeawayOrderRequestDTO {
    private String clientName;
    private String clientPhone;
    private String requirements;
    private String paymentMethod;
    private LocalDateTime receiptDate;
    private String receiptOption;
    private TakeawayOrderPositionRequestDTO[] takeawayOrderPositionList;

    public String getClientName() {
        return clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public String getReceiptOption() {
        return receiptOption;
    }

    public TakeawayOrderPositionRequestDTO[] getTakeawayOrderPositionList() {
        return takeawayOrderPositionList;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
        this.receiptDate = receiptDate;
    }

    public void setReceiptOption(String receiptOption) {
        this.receiptOption = receiptOption;
    }

    public void setTakeawayOrderPositionList(TakeawayOrderPositionRequestDTO[] takeawayOrderPositionList) {
        this.takeawayOrderPositionList = takeawayOrderPositionList;
    }
}
