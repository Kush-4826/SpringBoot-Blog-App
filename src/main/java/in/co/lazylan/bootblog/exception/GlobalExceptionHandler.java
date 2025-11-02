package in.co.lazylan.bootblog.exception;

import in.co.lazylan.bootblog.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Whenever there a ResourceNotFoundException is thrown in the System, this ExceptionHandler
     * will get executed, and in this exception handler, instead of breaking the flow, we gracefully
     * generate an error response and send that response to the user
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        String message = e.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(message);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
