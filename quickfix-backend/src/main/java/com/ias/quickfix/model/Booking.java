package com.ias.quickfix.model;

import lombok.Data;

@Data
public class Booking {
    private String id; // Booking ID
    private String providerId;
    private String jobId;
    private String status;
    private String notes;
}
