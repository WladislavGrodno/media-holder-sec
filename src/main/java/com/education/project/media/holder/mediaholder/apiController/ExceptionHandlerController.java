package com.education.project.media.holder.mediaholder.apiController;

import com.education.project.media.holder.mediaholder.dto.response.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {
    private static final String UNEXPECTED_EXCEPTION = "UNEXPECTED_EXCEPTION";
    private static final String VALIDATION_EXCEPTION =
            "Parameters validation not pass";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<StatusResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        Map<String, Object> details = new HashMap<>();
        exception.getFieldErrors().forEach(
                e -> details.put(e.getField(), e.getDefaultMessage()));
        return new ResponseEntity<>(
                new StatusResponse(
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        VALIDATION_EXCEPTION,
                        details),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<StatusResponse> handleServiceException(
            ServiceException exception) {
        String eMessage = exception.getMessage();
        log.error(eMessage, exception);
        return new ResponseEntity<>(
                new StatusResponse(eMessage, exception.toString()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusResponse> handleException(Exception exception) {
        String eMessage = exception.getMessage();
        switch (eMessage){
            case "OPERATION SUCCESSFUL" -> {
                log.info(eMessage);
                return new ResponseEntity<>(
                        new StatusResponse(eMessage, eMessage),
                        HttpStatus.NO_CONTENT
                );
            }
            case "NOT FOUND", "TABLE IS EMPTY" -> {
                log.info(eMessage);
                return new ResponseEntity<>(
                        new StatusResponse(eMessage, eMessage),
                        HttpStatus.NOT_FOUND
                );
            }
            default -> {
                log.error(eMessage);
                return new ResponseEntity<>(
                        new StatusResponse(UNEXPECTED_EXCEPTION, eMessage),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }
    }
}
