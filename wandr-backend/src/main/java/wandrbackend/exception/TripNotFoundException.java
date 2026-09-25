package wandrbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TripNotFoundException extends NotFoundException {
    public TripNotFoundException(String message) {
        super(message);
    }
}