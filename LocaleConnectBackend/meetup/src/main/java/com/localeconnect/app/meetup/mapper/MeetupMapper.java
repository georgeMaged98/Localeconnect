package com.localeconnect.app.meetup.mapper;

import com.localeconnect.app.meetup.dto.MeetupDTO;
import com.localeconnect.app.meetup.model.Meetup;
import org.mapstruct.Mapper;

@Mapper
public interface MeetupMapper {

    MeetupDTO toDomain(Meetup meetup);
    Meetup toEntity(MeetupDTO meetupDTO);
}
