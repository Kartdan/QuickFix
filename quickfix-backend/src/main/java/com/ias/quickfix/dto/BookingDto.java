package com.ias.quickfix.dto;

import lombok.Data;

@Data
public class BookingDto {
    private String providerId;
    private String jobId;
    private String status;
    private String notes;
}
