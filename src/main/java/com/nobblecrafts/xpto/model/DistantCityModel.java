package com.nobblecrafts.xpto.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=DistantCityImpl.class)
public interface DistantCityModel {

  Double getLatitude();
  Double getLongitude();
  String getCidade();
  Double getEq();
  
}
