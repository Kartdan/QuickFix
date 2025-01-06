package com.ias.quickfix.controller;

import com.ias.quickfix.dto.ReviewDto;
import com.ias.quickfix.service.ReviewService;
import com.ias.quickfix.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;  // Serviciul Provider pentru validarea provider-ului

    // 1. Adaugă o recenzie pentru un provider
    @PostMapping("/{providerEmail}")
    public ResponseEntity<?> addReview(@PathVariable String providerEmail, @RequestBody ReviewDto reviewDto) {
        try {
            // Verifică dacă provider-ul există
            if (!userService.providerExists(providerEmail)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Provider with email " + providerEmail + " not found");
            }

            ReviewDto savedReview = reviewService.addReview(providerEmail, reviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while adding the review");
        }
    }

    // 2. Obține toate recenziile pentru un provider cu email-ul specificat
    @GetMapping("/provider/{providerEmail}")
    public ResponseEntity<?> getReviewsForProvider(@PathVariable String providerEmail) {
        try {
            List<ReviewDto> reviews = reviewService.getAllReviewsForProvider(providerEmail);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while retrieving reviews");
        }
    }

    // 3. Șterge toate recenziile pentru un provider cu email-ul specificat
    @DeleteMapping("/{providerEmail}")
    public ResponseEntity<?> deleteAllReviewsForProvider(@PathVariable String providerEmail) {
        try {
            // Verifică dacă provider-ul există
            if (!userService.providerExists(providerEmail)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Provider with email " + providerEmail + " not found");
            }

            reviewService.deleteAllReviewsForProvider(providerEmail);
            return ResponseEntity.ok("All reviews deleted for provider with email: " + providerEmail);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while deleting reviews");
        }
    }

    // 4. Șterge o recenzie specifică pentru un provider (după reviewId)
    @DeleteMapping("/{providerEmail}/{reviewId}")
    public ResponseEntity<?> deleteReviewForProvider(@PathVariable String providerEmail, @PathVariable String reviewId) {
        try {
            // Verifică dacă provider-ul există
            if (!userService.providerExists(providerEmail)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Provider with email " + providerEmail + " not found");
            }

            reviewService.deleteReview(providerEmail, reviewId);
            return ResponseEntity.ok("Review deleted successfully for provider with email: " + providerEmail);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while deleting the review");
        }
    }

    // 5. Actualizează o recenzie pentru un provider (după reviewId)
    @PutMapping("/{providerEmail}/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable String providerEmail, @PathVariable String reviewId, @RequestBody ReviewDto reviewDto) {
        try {
            // Verifică dacă provider-ul există
            if (!userService.providerExists(providerEmail)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Provider with email " + providerEmail + " not found");
            }

            ReviewDto updatedReview = reviewService.updateReview(providerEmail, reviewId, reviewDto);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while updating the review");
        }
    }
}
