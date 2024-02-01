package com.localeconnect.app.trip.mapper;

import com.localeconnect.app.trip.dto.TripDTO;
import com.localeconnect.app.trip.dto.TripReviewDTO;
import com.localeconnect.app.trip.model.Trip;
import com.localeconnect.app.trip.model.TripReview;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TripReviewMapper {
    public TripReview toEntity(TripReviewDTO tripReviewDTO);
    public TripReviewDTO toDomain(TripReview tripReview);
    void updateTripReviewFromDto(TripReviewDTO tripReviewDTO, @MappingTarget TripReview entity);
}
