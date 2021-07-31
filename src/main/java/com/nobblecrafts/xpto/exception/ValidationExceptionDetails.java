package com.nobblecrafts.xpto.exception;

import com.nobblecrafts.xpto.exception.handler.ExceptionDetails;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ValidationExceptionDetails extends ExceptionDetails {
  private String fields;
  private String fieldsMessage;
}
