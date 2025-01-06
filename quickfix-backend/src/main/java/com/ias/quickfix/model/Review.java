package com.ias.quickfix.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private String reviewId;
    private String clientId;
    private int rating;
    private String reviewText;
}
