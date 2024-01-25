package com.localeconnect.app.meetup.service;

import com.localeconnect.app.meetup.dto.MeetupAttendDTO;
import com.localeconnect.app.meetup.dto.MeetupDTO;
import com.localeconnect.app.meetup.exceptions.ResourceNotFoundException;
import com.localeconnect.app.meetup.mapper.MeetupMapper;
import com.localeconnect.app.meetup.model.Meetup;
import com.localeconnect.app.meetup.repository.MeetupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MeetupService {

    private final MeetupRepository meetupRepository;
    private final MeetupMapper meetupMapper;

    public MeetupDTO createMeetup(MeetupDTO meetupDTO) {

        // check creatorId is valid

        // Save meetup in DATABASE
        Meetup meetup = meetupMapper.toEntity(meetupDTO);
        Meetup createdMeetup = meetupRepository.save(meetup);
        // return saved meetup
        return meetupMapper.toDomain(createdMeetup);
    }

    public void attendMeetup(Long meetupId, MeetupAttendDTO meetupAttendDTO){

        Optional<Meetup> meetup = meetupRepository.findById(meetupId);

        if(meetup.isEmpty()){
            throw new EntityNotFoundException("No Meetup Found with this meetupId!");
        }

        Long travellerId = meetupAttendDTO.getTravellerId();

        Meetup actualMeetup = meetup.get();

        actualMeetup.getMeetupAttendees().add(travellerId);

        meetupRepository.save(actualMeetup);
    }

    public void unattendMeetup(Long meetupId, MeetupAttendDTO meetupAttendDTO){

        Optional<Meetup> meetup = meetupRepository.findById(meetupId);

        if(meetup.isEmpty()){
            throw new EntityNotFoundException("No Meetup Found with this meetupId!");
        }

        Long travellerId = meetupAttendDTO.getTravellerId();

        Meetup actualMeetup = meetup.get();

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

        MeetupDTO meetupDTO = meetupMapper.toDomain(optional.get());

        meetupRepository.deleteById(id);

        return meetupDTO;
    }

}
