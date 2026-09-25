package wandrbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExpenseCategoryNotFoundException extends NotFoundException {
    public ExpenseCategoryNotFoundException(String message) { super(message); }
}

