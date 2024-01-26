package com.localeconnect.app.trip.service;

import com.localeconnect.app.trip.dto.NotificationDTO;
import com.localeconnect.app.trip.dto.TripDTO;
import com.localeconnect.app.trip.exceptions.ResourceNotFoundException;
import com.localeconnect.app.trip.mapper.TripMapper;
import com.localeconnect.app.trip.model.Trip;
import com.localeconnect.app.trip.repository.TripRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public TripDTO updateTrip( Long tripId, TripDTO tripDTO) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        if (optionalTrip.isEmpty())
            throw new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!");
        //update the trip
        Trip tripToUpdate = optionalTrip.get();
        tripMapper.updateTripFromDto(tripDTO, tripToUpdate);
        //send notifications to all travelers
        List<Long> travelers = tripToUpdate.getTravelers();
        for (Long traveler : travelers) {
            NotificationDTO newNotification = new NotificationDTO();
            newNotification.setTitle("New Notification");
            newNotification.setMessage("Meetup " + tripId +" Got Updated!");
            newNotification.setSentAt(LocalDateTime.now());
            newNotification.setReceiver(traveler);
            newNotification.setSender(tripToUpdate.getLocalguideId());
        }
        tripRepository.save(tripToUpdate);
        return tripMapper.toDomain(optionalTrip.get());
    }

    public void deleteTrip(Long tripId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        if (optionalTrip.isEmpty())
            throw new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!");
        //update the trip
        Trip tripToDelete = optionalTrip.get();
        //send notifications to all travelers
        List<Long> travelers = tripToDelete.getTravelers();
        for (Long traveler : travelers) {
            NotificationDTO newNotification = new NotificationDTO();
            newNotification.setTitle("New Notification");
            newNotification.setMessage("Meetup " + tripId +" Got Deleted!");
            newNotification.setSentAt(LocalDateTime.now());
            newNotification.setReceiver(traveler);
            newNotification.setSender(tripToDelete.getLocalguideId());
        }
        tripRepository.delete(tripToDelete);
    }
}
