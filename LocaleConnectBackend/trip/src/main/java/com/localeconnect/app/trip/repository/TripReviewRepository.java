package com.localeconnect.app.trip.repository;

import com.localeconnect.app.trip.model.TripReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripReviewRepository extends JpaRepository<TripReview, Long> {
}
