package com.ias.quickfix.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientDto {
    private String name;
    private String email;
    private String phone_number;
    private String address;
    private String password_hash;
    private List<BookingDto> bookings;
}
