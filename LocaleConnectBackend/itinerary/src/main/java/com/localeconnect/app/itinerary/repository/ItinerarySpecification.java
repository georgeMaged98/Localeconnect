package com.localeconnect.app.itinerary.repository;

import com.localeconnect.app.itinerary.model.Itinerary;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;


public class ItinerarySpecification {

    public static Specification<Itinerary> hasPlace(String place) {
        return (root, query, criteriaBuilder) -> {
            if (place == null || place.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Itinerary, String> tagsJoin = root.join("placesToVisit");
            return criteriaBuilder.equal(tagsJoin, place);
        };
    }

    public static Specification<Itinerary> hasTag(String tag) {
        return (root, query, criteriaBuilder) -> {
            if (tag == null || tag.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Itinerary, String> tagsJoin = root.join("tags");
            return criteriaBuilder.equal(tagsJoin, tag);
        };
    }


}

