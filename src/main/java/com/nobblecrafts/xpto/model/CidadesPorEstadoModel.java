package com.nobblecrafts.xpto.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CidadesPorEstadoImpl.class)
public interface CidadesPorEstadoModel extends Serializable {

  String getEstado();

  Long getCidades();

}
