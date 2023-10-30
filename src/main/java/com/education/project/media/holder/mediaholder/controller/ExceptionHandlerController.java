package com.education.project.media.holder.mediaholder.controller;

import com.education.project.media.holder.mediaholder.dto.response.StatusResponse;
import com.education.project.media.holder.mediaholder.exception.ExceptionAccessDenied;
import com.education.project.media.holder.mediaholder.exception.ExceptionNotFound;
import com.education.project.media.holder.mediaholder.exception.ExceptionOperationSuccessful;
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
        return switch (eMessage){
            case "Content-Type is not supported",
                    "Request method 'GET' is not supported",
                    "Request method 'PUT' is not supported",
                    "Request method 'DELETE' is not supported",
                    "Required request parameter 'userID' for method parameter type UUID is not present"
                    -> {
                log.error(eMessage);
                yield  new ResponseEntity<>(
                        new StatusResponse(
                                "We demand additional parameters.", eMessage ),
                        HttpStatus.BAD_REQUEST);
            }
            case "TABLE IS EMPTY" -> {
                log.info(eMessage);
                yield  new ResponseEntity<>(
                        new StatusResponse(eMessage, eMessage),
                        HttpStatus.NOT_FOUND);
            }
            default -> {
                log.error(eMessage);
                yield  new ResponseEntity<>(
                        new StatusResponse(UNEXPECTED_EXCEPTION, eMessage),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };
    }

    @ExceptionHandler(ExceptionOperationSuccessful.class)
    public ResponseEntity<StatusResponse> handleOperationSuccessful() {
        String message = "OPERATION SUCCESSFUL";
        log.info(message);
        return new ResponseEntity<>(
                new StatusResponse(message,message), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ExceptionAccessDenied.class)
    public ResponseEntity<StatusResponse> handleAccessDeniedException() {
        String message = "ACCESS DENIED";
        log.info(message);
        return new ResponseEntity<>(
                new StatusResponse(message, message), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExceptionNotFound.class)
    public ResponseEntity<StatusResponse> handleExceptionNotFound() {
        String message = "NOT FOUND";
        log.info(message);
        return new ResponseEntity<>(
                new StatusResponse(message, message), HttpStatus.NOT_FOUND);
    }
}
