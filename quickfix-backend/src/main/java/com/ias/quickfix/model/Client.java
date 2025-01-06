package com.ias.quickfix.model;

import com.ias.quickfix.dto.BookingDto;
import lombok.Data;

import java.util.List;

@Data
public class Client {
    private String name;
    private String email;
    private String phone_number;
    private String address;
    private String password_hash;
    private List<Booking> bookings;
}
