package in.co.lazylan.bootblog.exception;

import in.co.lazylan.bootblog.response.ErrorResponse;
import in.co.lazylan.bootblog.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
