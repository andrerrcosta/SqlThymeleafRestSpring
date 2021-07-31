package com.nobblecrafts.xpto.model;

import lombok.Data;

@Data
public class DistantCityImpl implements DistantCityModel {

  Double latitude;
  Double longitude;
  Double eq;
  String cidade;
  
}
