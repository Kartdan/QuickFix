package com.ias.quickfix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDto {
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
}
