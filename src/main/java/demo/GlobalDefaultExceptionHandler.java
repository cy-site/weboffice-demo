package demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RestClientResponseException.class)
    @ResponseBody String handleBadRequest(RestClientResponseException ex) {
        return ex.getResponseBodyAsString();
    }
    
}
