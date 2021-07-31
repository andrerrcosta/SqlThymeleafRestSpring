package com.nobblecrafts.xpto.util;

import java.util.function.Function;

public abstract class ModelMapper<T, U> {

  private final Function<T, U> fromCsv;
  private final Function<U, T> fromEntity;

  public ModelMapper(final Function<T, U> fromCsv, final Function<U, T> fromEntity) {
    this.fromCsv = fromCsv;
    this.fromEntity = fromEntity;
  }

  public final U convertFromCsvToEntity(final T csv) {
    return fromCsv.apply(csv);
  }

  public final T convertFromEntityToCsv(final U entity) {
    return fromEntity.apply(entity);
  }
}
