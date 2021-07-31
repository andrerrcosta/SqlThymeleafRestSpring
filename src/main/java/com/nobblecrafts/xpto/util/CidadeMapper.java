package com.nobblecrafts.xpto.util;

import java.util.Random;

import com.nobblecrafts.xpto.model.CidadeModel;

public class CidadeMapper extends ModelMapper<String, CidadeModel> {

  public CidadeMapper() {
    super(CidadeMapper::convertToCidadeModel, CidadeMapper::convertToCsv);
  }

  private static String convertToCsv(CidadeModel cidade) {
    return "";
  }

  private static CidadeModel convertToCidadeModel(String csv) {
    String[] split = csv.split(",");

    Long ibgeId = null;
    Double longitude = null;
    Double latitude = null;
    var random = new Random();

    try {
      ibgeId = Long.parseLong(split[0]);
    } catch (NumberFormatException e) {
      ibgeId = random.nextLong();
    }

    try {
      longitude = Double.parseDouble(split[4]);
    } catch (NumberFormatException e) {
      longitude = 0D;
    }

    try {
      latitude = Double.parseDouble(split[5]);
    } catch (NumberFormatException e) {
      latitude = 0D;
    }

    return CidadeModel.builder().ibgeId(ibgeId).uf(split[1]).name(split[2])
        .capital(split[3].equalsIgnoreCase("true")).longitude(longitude)
        .latitude(latitude).noAccents(split[6]).altNames(split[7]).microregion(split[8])
        .mesoregion(split[9]).build();
  }

}
