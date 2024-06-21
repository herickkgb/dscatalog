package br.com.herick.dscatalog.resources.exceptions;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.herick.dscatalog.services.exceptions.DataBaseException;
import br.com.herick.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExeptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(ResourceExeptionHandler.class);
    

    // ####Exemplo de tratamento de validação de argumentos ####
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<StandardError> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
    //     HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    //     String error = "Validation Error";
    //     StandardError err = new StandardError();
    //     err.setTimestamp(Instant.now());
    //     err.setStatus(status.value());
    //     err.setError(error);
    //     err.setMessage(e.getBindingResult().getAllErrors().stream()
    //                 .map(ObjectError::getDefaultMessage)
    //                 .collect(Collectors.joining(", ")));
    //     err.setPath(request.getRequestURI());
    //     logger.error("Validation exception: {}", err.getMessage());
    //     return ResponseEntity.status(status).body(err);
    // }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        return buildResponseEntity(e, request, HttpStatus.NOT_FOUND, "Resource not found");
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> handleDataBaseException(DataBaseException e, HttpServletRequest request) {
        return buildResponseEntity(e, request, HttpStatus.BAD_REQUEST, "Database Exception");
    }

    private ResponseEntity<StandardError> buildResponseEntity(Exception e, HttpServletRequest request, HttpStatus status, String error) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(error);
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        
        logger.error("Exception: {} - {}", error, e.getMessage());
        
        return ResponseEntity.status(status).body(err);
    }

}
