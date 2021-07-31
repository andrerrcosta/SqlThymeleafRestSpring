package com.nobblecrafts.xpto.exception.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.nobblecrafts.xpto.exception.BadRequestException;
import com.nobblecrafts.xpto.exception.BadRequestExceptionDetails;
import com.nobblecrafts.xpto.exception.ValidationExceptionDetails;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
      HttpStatus status, WebRequest request) {

    var httpStatus = status.value();
    var title = ex.getCause();
    var message = ex.getMessage();

    var details = ExceptionDetails.builder().timestamp(new Date()).status(httpStatus)
        .title(title != null ? title.getMessage() : null).details(message).build();

    return new ResponseEntity<>(details, headers, status);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException(BadRequestException exception) {
    return new ResponseEntity<>(BadRequestExceptionDetails.builder().timestamp(new Date())
        .status(HttpStatus.BAD_REQUEST.value()).title("Bad Request").details(exception.getMessage()).build(),
        HttpStatus.BAD_REQUEST);
  }

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {

    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    var fields = errors.stream().map(FieldError::getField).collect(Collectors.joining(","));
    var message = errors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));

    return new ResponseEntity<>(
        ValidationExceptionDetails.builder().timestamp(new Date()).status(HttpStatus.BAD_REQUEST.value())
            .title("Invalid Fields").details(ex.getMessage()).fields(fields).fieldsMessage(message).build(),
        HttpStatus.BAD_REQUEST);
  }

}
