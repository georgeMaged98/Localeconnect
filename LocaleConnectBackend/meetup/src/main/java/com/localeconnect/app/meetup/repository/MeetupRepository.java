package com.localeconnect.app.meetup.repository;

import com.localeconnect.app.meetup.model.Meetup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetupRepository extends JpaRepository<Meetup, Long> {
    List<Meetup> findByCreatorId(Long creatorId);

}
