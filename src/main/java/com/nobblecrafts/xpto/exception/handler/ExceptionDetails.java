package com.nobblecrafts.xpto.exception.handler;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ExceptionDetails {

  String title;
  int status;
  String details;
  String message;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", locale = "pt-BR", timezone = "Brazil/East")
  Date timestamp;
  
}
