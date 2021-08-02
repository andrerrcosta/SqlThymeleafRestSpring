package com.nobblecrafts.xpto.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=DistantCityImpl.class)
public interface DistantCityModel extends Serializable {

  Double getLatitude();
  Double getLongitude();
  String getCidade();
  Double getEq();
  
}
