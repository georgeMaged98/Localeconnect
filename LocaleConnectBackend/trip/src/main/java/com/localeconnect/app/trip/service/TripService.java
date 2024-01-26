package com.localeconnect.app.trip.service;

import com.localeconnect.app.trip.dto.TripDTO;
import com.localeconnect.app.trip.exceptions.ResourceNotFoundException;
import com.localeconnect.app.trip.mapper.TripMapper;
import com.localeconnect.app.trip.model.Trip;
import com.localeconnect.app.trip.repository.TripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripService {
    private TripRepository tripRepository;
    private TripMapper tripMapper;

    public List<TripDTO> getAllTrips() {
       return tripRepository.findAll().stream()
               .map(trip -> tripMapper.toDomain(trip))
               .collect(Collectors.toList());
    }

    public TripDTO getById(Long tripId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        if (optionalTrip.isEmpty())
            throw new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!");
        return tripMapper.toDomain(optionalTrip.get());
    }
}
