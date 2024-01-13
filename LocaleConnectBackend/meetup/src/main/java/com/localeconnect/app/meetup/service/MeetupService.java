package com.localeconnect.app.meetup.service;

import com.localeconnect.app.meetup.mapper.MeetupMapper;
import com.localeconnect.app.meetup.repository.MeetupRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MeetupService {

    private MeetupRepository meetupRepository;
    private MeetupMapper meetupMapper;


}
