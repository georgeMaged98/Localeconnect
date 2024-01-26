package com.localeconnect.app.trip.mapper;

import com.localeconnect.app.trip.dto.TripDTO;
import com.localeconnect.app.trip.model.Trip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripMapper {
    public Trip toEntity(TripDTO tripDTO);
    public TripDTO toDomain(Trip trip);
}
