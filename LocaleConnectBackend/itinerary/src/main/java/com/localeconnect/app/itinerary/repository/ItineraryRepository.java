package com.localeconnect.app.itinerary.repository;

import com.localeconnect.app.itinerary.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
}

