package com.nobblecrafts.xpto.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CidadesPorEstadoImpl.class)
public interface CidadesPorEstadoModel {

  String getEstado();

  Long getCidades();

}
