package com.ias.quickfix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDto {
    private String name;
    private String email;
    private String phone_number;
    private String address;
    private String password_hash;

    private List<JobDto> jobs;
    private List<AvailabilityDto> availability;
    private List<BookingDto> bookings;
    private List<ReviewDto> reviews;
}
