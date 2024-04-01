package codehanzoom.greenwalk.global.exceptionhandler;

import codehanzoom.greenwalk.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.dao.DuplicateKeyException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseDto<String> handleDuplicateKeyException(Exception e)
    {
        return new ResponseDto<String>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseDto<String> handleArgumentException(Exception e)
    {
        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
