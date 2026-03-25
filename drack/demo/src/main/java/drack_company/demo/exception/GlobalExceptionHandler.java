package drack_company.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex){
       ApiError error = new ApiError(
               HttpStatus.BAD_REQUEST.value(),// Status 400
               ex.getMessage(),
               LocalDateTime.now()
       );
       return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
   }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(), // Status 404
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
