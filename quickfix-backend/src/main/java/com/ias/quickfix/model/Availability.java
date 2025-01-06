package com.ias.quickfix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
    private String availabilityId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}
