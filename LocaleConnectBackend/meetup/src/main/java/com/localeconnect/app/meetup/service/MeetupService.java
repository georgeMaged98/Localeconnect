package com.localeconnect.app.meetup.service;

import com.localeconnect.app.meetup.config.MeetupRabbitConfig;
import com.localeconnect.app.meetup.dto.MeetupAttendDTO;
import com.localeconnect.app.meetup.dto.MeetupDTO;
//import com.localeconnect.app.meetup.dto.MeetupEditDTO;
import com.localeconnect.app.meetup.dto.MeetupEditDTO;
import com.localeconnect.app.meetup.dto.NotificationDTO;
import com.localeconnect.app.meetup.exceptions.LogicException;
import com.localeconnect.app.meetup.exceptions.ResourceNotFoundException;
import com.localeconnect.app.meetup.mapper.MeetupMapper;
import com.localeconnect.app.meetup.model.Meetup;
import com.localeconnect.app.meetup.rabbit.RabbitMQMessageProducer;
import com.localeconnect.app.meetup.repository.MeetupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MeetupService {

    private final MeetupRepository meetupRepository;
    private final MeetupMapper meetupMapper;

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public MeetupDTO createMeetup(MeetupDTO meetupDTO) {

        // check creatorId is valid

        // Save meetup in DATABASE
        Meetup meetup = meetupMapper.toEntity(meetupDTO);
        Meetup createdMeetup = meetupRepository.save(meetup);
        // return saved meetup
        return meetupMapper.toDomain(createdMeetup);
    }

    public MeetupDTO updateMeetup(MeetupEditDTO meetupEditDTO, Long meetupId) {

        Optional<Meetup> optional = meetupRepository.findById(meetupId);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Meetup Found with id: " + meetupId + "!");

        Meetup actualMeetup = optional.get();
        if (meetupEditDTO.getName() != null)
            actualMeetup.setName(meetupEditDTO.getName());

        if (meetupEditDTO.getDescription() != null)
            actualMeetup.setDescription(meetupEditDTO.getDescription());

        if (meetupEditDTO.getCost() != null)
            actualMeetup.setCost(meetupEditDTO.getCost());

        if (meetupEditDTO.getDate() != null)
            actualMeetup.setDate(meetupEditDTO.getDate());

        if (meetupEditDTO.getStartTime() != null)
            actualMeetup.setStartTime(meetupEditDTO.getStartTime());

        if (meetupEditDTO.getEndTime() != null)
            actualMeetup.setEndTime(meetupEditDTO.getEndTime());

        if (meetupEditDTO.getLocation() != null)
            actualMeetup.setLocation(meetupEditDTO.getLocation());

        if (meetupEditDTO.getSpokenLanguages() != null)
            actualMeetup.setSpokenLanguages(meetupEditDTO.getSpokenLanguages());

        meetupRepository.save(actualMeetup);

        List<Long> attendees = actualMeetup.getMeetupAttendees();
        for (Long att:attendees
             ) {
            NotificationDTO newNotification = new NotificationDTO();
            newNotification.setTitle("New Notification");
            newNotification.setMessage("Meetup " + meetupId +" Got Updated!");
            newNotification.setSentAt(LocalDateTime.now());
            newNotification.setReceiverID(att);
            newNotification.setSenderID(actualMeetup.getCreatorId());
            rabbitMQMessageProducer.publish(newNotification, MeetupRabbitConfig.EXCHANGE, MeetupRabbitConfig.ROUTING_KEY);
        }
        return meetupMapper.toDomain(actualMeetup);
    }


    public void attendMeetup(Long meetupId, MeetupAttendDTO meetupAttendDTO) {

        Optional<Meetup> meetup = meetupRepository.findById(meetupId);
        if (meetup.isEmpty())
            throw new ResourceNotFoundException("No Meetup Found with id: " + meetupId + "!");

        Long travellerId = meetupAttendDTO.getTravellerId();
        Meetup actualMeetup = meetup.get();

        if (actualMeetup.getMeetupAttendees().contains(travellerId))
            throw new LogicException("Traveller is ALREADY in meetup attendees!");

        actualMeetup.getMeetupAttendees().add(travellerId);
        meetupRepository.save(actualMeetup);
    }

    public void unattendMeetup(Long meetupId, MeetupAttendDTO meetupAttendDTO) {

        Optional<Meetup> meetup = meetupRepository.findById(meetupId);
        if (meetup.isEmpty())
            throw new ResourceNotFoundException("No Meetup Found with id: " + meetupId + "!");

        Long travellerId = meetupAttendDTO.getTravellerId();
        Meetup actualMeetup = meetup.get();
        if (!actualMeetup.getMeetupAttendees().contains(travellerId))
            throw new LogicException("Traveller is NOT in meetup attendees!");

        actualMeetup.getMeetupAttendees().remove(travellerId);
        meetupRepository.save(actualMeetup);
    }

    public List<MeetupDTO> getAllMeetups() {
        List<Meetup> itineraries = meetupRepository.findAll();
        return itineraries.stream().map(
                        meetupMapper::toDomain)
                .collect(Collectors.toList());
    }

    public MeetupDTO getMeetupById(Long id) {
        Optional<Meetup> optional = meetupRepository.findById(id);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Meetup Found with id: " + id + "!");

        return meetupMapper.toDomain(optional.get());
    }

    public MeetupDTO deleteMeetupById(Long id) {
        Optional<Meetup> optional = meetupRepository.findById(id);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Meetup Found with id: " + id + "!");

        Meetup actualMeetup = optional.get();
        meetupRepository.deleteById(id);
        //TODO: NOTIFY ATTENDEES
        List<Long> attendees = actualMeetup.getMeetupAttendees();
        for (Long att:attendees
        ) {
            NotificationDTO newNotification = new NotificationDTO();
            newNotification.setTitle("New Notification");
            newNotification.setMessage("Meetup " + actualMeetup.getId() +" Got Updated!");
            newNotification.setSentAt(LocalDateTime.now());
            newNotification.setReceiverID(att);
            newNotification.setSenderID(actualMeetup.getCreatorId());
            rabbitMQMessageProducer.publish(newNotification, MeetupRabbitConfig.EXCHANGE, MeetupRabbitConfig.ROUTING_KEY);
        }

        MeetupDTO meetupDTO = meetupMapper.toDomain(optional.get());
        return meetupDTO;
    }

}
