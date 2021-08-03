package com.limbae.pfy.exception;

import com.limbae.pfy.dto.ResponseObjectDTO;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.io.NotActiveException;

@RestControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice {

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ResponseObjectDTO> MissingRequestValueException(MissingRequestValueException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ResponseObjectDTO(e.getMessage()), HttpStatus.BAD_REQUEST); //TODO http status 변경할것
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseObjectDTO> IOException(IOException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ResponseObjectDTO(e.getMessage()), HttpStatus.BAD_REQUEST); //TODO http status 변경할것
    }

    @ExceptionHandler(NotSupportedException.class)
    public ResponseEntity<ResponseObjectDTO> NotSupportedException(NotSupportedException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ResponseObjectDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotActiveException.class)
    public ResponseEntity<ResponseObjectDTO> NotActiveException(NotActiveException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ResponseObjectDTO(e.getMessage()), HttpStatus.NOT_ACCEPTABLE); //TODO http status 변경할것
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ResponseObjectDTO> AuthException(DuplicateMemberException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ResponseObjectDTO(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseObjectDTO> NotFoundExceptionException(NotFoundException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ResponseObjectDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ResponseObjectDTO> AuthException(AuthException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new ResponseObjectDTO(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObjectDTO> Exception(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new ResponseObjectDTO("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
