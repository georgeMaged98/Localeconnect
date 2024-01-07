package com.localeconnect.app.itinerary;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.model.Itinerary;
import com.localeconnect.app.itinerary.repository.ItineraryRepository;
import com.localeconnect.app.itinerary.service.ItineraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItineraryServiceTest {

    @Mock
    private ItineraryRepository itineraryRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ItineraryService itineraryService;

    @BeforeEach
    void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), (Object) any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void createItineraryForValidUserId() {
        String validUserId = "1";
        ItineraryDTO dto = new ItineraryDTO();
        Itinerary mockItinerary = new Itinerary();
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));
        when(itineraryRepository.save(any(Itinerary.class))).thenReturn(mockItinerary);
        Itinerary result = itineraryService.createItinerary(dto, validUserId);
        assertNotNull(result);
    }

    @Test
    public void notCreateItineraryForInvalidUserId() {
        String invalidUserId = "2";
        ItineraryDTO dto = new ItineraryDTO();
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(false));
        assertThrows(IllegalArgumentException.class, () -> {
            itineraryService.createItinerary(dto, invalidUserId);
        });
    }
}
