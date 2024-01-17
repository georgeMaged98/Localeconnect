package com.localeconnect.app.meetup.service;

import com.localeconnect.app.meetup.dto.MeetupDTO;
import com.localeconnect.app.meetup.mapper.MeetupMapper;
import com.localeconnect.app.meetup.model.Meetup;
import com.localeconnect.app.meetup.repository.MeetupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MeetupService {

    private final MeetupRepository meetupRepository;
    private final MeetupMapper meetupMapper;

    public MeetupDTO createMeetup(MeetupDTO meetupDTO){

        // check creatorId is valid

        // Save meetup in DATABASE
        Meetup meetup = meetupMapper.toEntity(meetupDTO);
        Meetup createdMeetup = meetupRepository.save(meetup);

        // return saved meetup

        return meetupMapper.toDomain(createdMeetup);
    }

    public MeetupDTO updateMeetup(MeetupDTO meetupDTO, Long id) {
        Meetup meetup = meetupMapper.toEntity(meetupDTO);

        if (!meetupRepository.existsById(id)){
            throw new EntityNotFoundException("Meetup does not exist!");
        }

        meetup.setId(id);

        meetupRepository.save(meetup);
        return meetupMapper.toDomain(meetup);

    }

}
