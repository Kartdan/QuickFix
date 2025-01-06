package com.ias.quickfix.service;

import com.ias.quickfix.dto.ProviderDto;
import com.ias.quickfix.dto.ReviewDto;
import com.ias.quickfix.exceptions.ProviderNotFoundException;
import com.ias.quickfix.model.Review;
import com.ias.quickfix.model.Provider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ReviewService {

    private final FirebaseService firebaseService;
    private final ModelMapper modelMapper;
    private final String PROVIDER_COLLECTION = "providers"; // Colectia Firestore pentru providers

    // 1. Adăugarea unui review pentru un provider
    public ReviewDto addReview(String providerEmail, ReviewDto reviewDto) {
        // Verifică dacă provider-ul există
        Provider provider = firebaseService.getObjectById(PROVIDER_COLLECTION, providerEmail, Provider.class);
        if (provider == null) {
            throw new ProviderNotFoundException("Provider with email " + providerEmail + " not found");
        }

        // Creează un review nou
        Review review = modelMapper.map(reviewDto, Review.class);

        // Adaugă review-ul la lista de recenzii a provider-ului
        if (provider.getReviews() == null) {
            provider.setReviews(new ArrayList<>());
        }
        provider.getReviews().add(review);

        // Salvează provider-ul cu lista actualizată de recenzii
        firebaseService.saveObject(PROVIDER_COLLECTION, providerEmail, provider);

        log.info("Added review for provider {}: {}", providerEmail, review);

        return modelMapper.map(review, ReviewDto.class);
    }

    // 2. Obține toate recenziile pentru un provider
    public List<ReviewDto> getAllReviewsForProvider(String providerEmail) {
        // Verifică dacă provider-ul există
        Provider provider = firebaseService.getObjectById(PROVIDER_COLLECTION, providerEmail, Provider.class);
        if (provider == null) {
            throw new ProviderNotFoundException("Provider with email " + providerEmail + " not found");
        }

        // Returnează lista de recenzii a provider-ului
        return provider.getReviews().stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    // 3. Șterge toate recenziile pentru un provider
    public void deleteAllReviewsForProvider(String providerEmail) {
        // Verifică dacă provider-ul există
        Provider provider = firebaseService.getObjectById(PROVIDER_COLLECTION, providerEmail, Provider.class);
        if (provider == null) {
            throw new ProviderNotFoundException("Provider with email " + providerEmail + " not found");
        }

        // Șterge toate recenziile
        provider.setReviews(null); // Golește lista de recenzii
        firebaseService.saveObject(PROVIDER_COLLECTION, providerEmail, provider);

        log.info("Deleted all reviews for provider {}", providerEmail);
    }

    // 4. Șterge o recenzie specifică pentru un provider
    public void deleteReview(String providerEmail, String reviewId) {
        // Verifică dacă provider-ul există
        Provider provider = firebaseService.getObjectById(PROVIDER_COLLECTION, providerEmail, Provider.class);
        if (provider == null) {
            throw new ProviderNotFoundException("Provider with email " + providerEmail + " not found");
        }

        // Căutăm recenzia după ID
        Review reviewToDelete = provider.getReviews().stream()
                .filter(review -> review.getReviewId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review with ID " + reviewId + " not found"));

        // Șterge recenzia din lista de recenzii
        provider.getReviews().remove(reviewToDelete);
        firebaseService.saveObject(PROVIDER_COLLECTION, providerEmail, provider);

        log.info("Deleted review with ID {} for provider {}", reviewId, providerEmail);
    }

    // 5. Actualizează o recenzie pentru un provider
    public ReviewDto updateReview(String providerEmail, String reviewId, ReviewDto reviewDto) {
        // Verifică dacă provider-ul există
        Provider provider = firebaseService.getObjectById(PROVIDER_COLLECTION, providerEmail, Provider.class);
        if (provider == null) {
            throw new ProviderNotFoundException("Provider with email " + providerEmail + " not found");
        }

        // Căutăm recenzia de actualizat
        Review reviewToUpdate = provider.getReviews().stream()
                .filter(review -> review.getReviewId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review with ID " + reviewId + " not found"));

        // Actualizăm recenzia
        reviewToUpdate.setRating(reviewDto.getRating());
        reviewToUpdate.setReviewText(reviewDto.getReviewText());

        // Salvează provider-ul cu recenzia actualizată
        firebaseService.saveObject(PROVIDER_COLLECTION, providerEmail, provider);

        log.info("Updated review with ID {} for provider {}", reviewId, providerEmail);

        return modelMapper.map(reviewToUpdate, ReviewDto.class);
    }
}

