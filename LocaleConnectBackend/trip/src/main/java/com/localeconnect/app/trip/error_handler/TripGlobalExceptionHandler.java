package com.localeconnect.app.trip.error_handler;

import com.localeconnect.app.trip.exceptions.ResourceNotFoundException;
import com.localeconnect.app.trip.response_handler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class TripGlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundExceptions(Exception e){
        HttpStatus status = HttpStatus.NOT_FOUND; //404
        List<String> errorMessages = Arrays.asList(e.getMessage());
        System.out.println("ResourceNotFoundException: " + errorMessages);

        ErrorResponse errorResponse = new ErrorResponse(
                status,
                errorMessages
        );
        return ResponseHandler.generateResponse("Error!", status, null, errorResponse);
    }


}
