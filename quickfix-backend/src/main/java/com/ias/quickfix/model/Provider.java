package com.ias.quickfix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
    private String name;
    private String email;
    private String phone_number;
    private String address;
    private String password_hash;

    private List<Job> jobs;
    private List<Availability> availability;
    private List<Booking> bookings;
    private List<Review> reviews;
}
