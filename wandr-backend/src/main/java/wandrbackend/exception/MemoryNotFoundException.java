package wandrbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemoryNotFoundException extends NotFoundException {
    public MemoryNotFoundException(String message) {
        super(message);
    }
}
